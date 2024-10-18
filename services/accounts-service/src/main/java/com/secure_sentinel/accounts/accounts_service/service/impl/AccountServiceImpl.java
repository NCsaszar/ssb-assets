package com.secure_sentinel.accounts.accounts_service.service.impl;

import com.secure_sentinel.accounts.accounts_service.dao.AccountProgramRepository;
import com.secure_sentinel.accounts.accounts_service.dao.AccountRepository;
import com.secure_sentinel.accounts.accounts_service.dao.TransactionRepository;
import com.secure_sentinel.accounts.accounts_service.dto.*;
import com.secure_sentinel.accounts.accounts_service.exceptions.AccountNotFoundException;
import com.secure_sentinel.accounts.accounts_service.exceptions.UserNotFoundException;
import com.secure_sentinel.accounts.accounts_service.mapper.AccountMapper;
import com.secure_sentinel.accounts.accounts_service.mapper.TransactionMapper;
import com.secure_sentinel.accounts.accounts_service.model.Account;
import com.secure_sentinel.accounts.accounts_service.model.AccountProgram;
import com.secure_sentinel.accounts.accounts_service.model.Transaction;
import com.secure_sentinel.accounts.accounts_service.model.enums.AccountType;
import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import com.secure_sentinel.accounts.accounts_service.service.AccountService;
import com.secure_sentinel.accounts.accounts_service.utils.AccountUtils;
import com.secure_sentinel.accounts.accounts_service.utils.TransactionUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    private final AccountProgramRepository accountProgramRepository;

    @Override
    public Page<AccountDTO> getAllAccounts(int page, int size, String sortBy, String accountType,
                                           Double minBalance, Double maxBalance, Boolean isActive,
                                           LocalDateTime startDate, LocalDateTime endDate) {
        // Validate Inputs
        validateInputs(minBalance, maxBalance, startDate, endDate);
        // One field one direction sorting
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortBy.split(",")[1]),
                sortBy.split(",")[0]));
        // Fetch accounts with filters
        Page<Account> accounts = accountRepository.findAllWithFilters(
                accountType, minBalance, maxBalance, isActive, startDate, endDate, pageRequest
        );

        return accounts.map(accountMapper::accountToAccountDTO);
    }

    @Override
    public AccountDTO getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return accountMapper.accountToAccountDTO(account);
    }

    @Override
    public DetailedAccountDTO getAccountDetails(Integer accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        List<Transaction> transactions =
                transactionRepository.findTop10ByAccountAccountIdOrderByDateTimeDesc(accountId);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::transactionToTransactionDTO)
                .toList();


        return DetailedAccountDTO.builder()
                .accountNumber(AccountUtils.maskAccountNumber(account.getAccountNumber()))
                .accountType(account.getAccountType().toString())
                .balance(account.getBalance())
                .creditLimit(account.getCreditLimit())
                .amountOwed(account.getAmountOwed())
                .programName(account.getAccountProgram().getProgramName())
                .transactions(transactionDTOs)
                .build();
    }

    @Override
    public List<AccountDTO> getAccountsByUserId(Integer userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()) {
            throw new UserNotFoundException("No accounts found for user: " + userId);
        }
        return accounts.stream()
                .map(accountMapper::accountToAccountDTO)
                .toList();
    }

    @Override
    public List<AccountDTO> getActiveAccountsByUserId(Integer userId) {
        List<Account> accounts = accountRepository.findByUserIdAndIsActive(userId, true);
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No active accounts found for user: " + userId);
        }

        return accounts.stream()
                .map(accountMapper::accountToAccountDTO)
                .toList();
    }

    @Override
    @Transactional
    public AccountDTO createAccount(AccountCreationRequestDTO request) {
        AccountType accountType = AccountType.valueOf(request.getAccountType().toUpperCase());
        AccountProgram accountProgram =
                accountProgramRepository.findByProgramNameAndAccountType(request.getProgramName(), accountType.name())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid account program name"));

        String accountNumber = AccountUtils.generateAccountNumber(request.getUserId());
        Account newAccount = new Account();
        newAccount.setUserId(request.getUserId());
        newAccount.setAccountNumber(accountNumber);
        newAccount.setBalance(request.getInitialBalance());
        newAccount.setAccountType(accountType);
        newAccount.setAccountProgram(accountProgram);
        Account savedAccount = accountRepository.save(newAccount);

        if (request.getInitialBalance() > 0) {
            TransactionUtils.createAndSaveTransaction(transactionRepository, newAccount, request.getInitialBalance(),
                    TransactionType.DEPOSIT, "Initial deposit", false);
        }

        return accountMapper.accountToAccountDTO(savedAccount);
    }

    @Override
    @Transactional
    public void deactivateAccount(Integer accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID: " + accountId + " not found"));

        account.setIsActive(false);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void activateAccount(Integer accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID: " + accountId + " not found"));

        account.setIsActive(true);
        accountRepository.save(account);
    }

    protected void validateInputs(Double minBalance, Double maxBalance, LocalDateTime startDate,
                                  LocalDateTime endDate) {
        if (minBalance != null && maxBalance != null && minBalance > maxBalance) {
            throw new IllegalArgumentException("Minimum balance cannot be greater than maximum balance.");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
    }

    @Override
    @Transactional
    public void processFile(MultipartFile file) throws IOException {
        if (file.getContentType().equals("text/csv")) {
            processCSV(file.getInputStream());
        } else {
            processExcel(file.getInputStream());
        }
    }

    private void processCSV(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT
                             .builder()
                             .setHeader()
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true)
                             .build())) {

            for (CSVRecord csvRecord : csvParser) {
                System.out.println("Processing record: " + csvRecord);
                try {
                    String userId = csvRecord.get("UserId");
                    String accountType = csvRecord.get("AccountType");
                    System.out.println("UserId: " + userId + ", AccountType: " + accountType);
                    createUploadedAccount(userId, accountType);
                } catch (Exception e) {
                    throw new RuntimeException("Error processing CSV record: " + csvRecord, e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error parsing CSV file", e);
        }
    }

    private void processExcel(InputStream is) throws IOException {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            Cell userIdCell = row.getCell(0);
            Cell accountTypeCell = row.getCell(1);
            createUploadedAccount(userIdCell.toString(), accountTypeCell.toString());
        }
    }

    private void createUploadedAccount(String userId, String accountTypeString) {
        String accountNumber = AccountUtils.generateAccountNumber(Integer.valueOf(userId));
        Account newAccount = new Account();
        newAccount.setUserId(Integer.valueOf(userId));
        newAccount.setAccountNumber(accountNumber);
        newAccount.setBalance(0.0); // Needs to be improved for account type whether initial balance or starting credit
        AccountType accountType = AccountType.forValue(accountTypeString);
        newAccount.setAccountType(accountType);
        accountRepository.save(newAccount);
    }

    @Override
    public List<AccountDTO> getAllAccountsForBatch() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(accountMapper::accountToAccountDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountWIthTransactions> getAllAccountsWithTransactions(LocalDateTime startDate,
                                                                        LocalDateTime endDate) {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> {
                    List<Transaction> transactions = transactionRepository.findAllByAccountAndDateRange(
                            account.getAccountId(), startDate, endDate);
                    List<TransactionDTO> transactionDTOS = transactions.stream()
                            .map(transactionMapper::transactionToTransactionDTO)
                            .toList();
                    return AccountWIthTransactions.builder()
                            .account(accountMapper.accountToAccountDTO(account))
                            .transactions(transactionDTOS)
                            .build();
                })
                .collect(Collectors.toList());
    }
}