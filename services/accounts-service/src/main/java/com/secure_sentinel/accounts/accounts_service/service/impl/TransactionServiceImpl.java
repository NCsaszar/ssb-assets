package com.secure_sentinel.accounts.accounts_service.service.impl;

import com.secure_sentinel.accounts.accounts_service.dao.AccountRepository;
import com.secure_sentinel.accounts.accounts_service.dao.TransactionRepository;
import com.secure_sentinel.accounts.accounts_service.dto.DepositFundsDTO;
import com.secure_sentinel.accounts.accounts_service.dto.PaymentDTO;
import com.secure_sentinel.accounts.accounts_service.dto.TransactionDTO;
import com.secure_sentinel.accounts.accounts_service.dto.TransferFundsDTO;
import com.secure_sentinel.accounts.accounts_service.exceptions.*;
import com.secure_sentinel.accounts.accounts_service.mapper.TransactionMapper;
import com.secure_sentinel.accounts.accounts_service.model.Account;
import com.secure_sentinel.accounts.accounts_service.model.AccountProgram;
import com.secure_sentinel.accounts.accounts_service.model.Transaction;
import com.secure_sentinel.accounts.accounts_service.model.enums.AccountType;
import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import com.secure_sentinel.accounts.accounts_service.service.RetryUtility;
import com.secure_sentinel.accounts.accounts_service.service.TransactionOperations;
import com.secure_sentinel.accounts.accounts_service.service.TransactionService;
import com.secure_sentinel.accounts.accounts_service.utils.FileExportUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;
    private final TransactionOperations transactionOperations;
    private final RetryUtility retryUtility;
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    /**
     * Retrieves a page of transactions for a specific account based on various filters.
     *
     * @param accountId       the account ID to retrieve transactions for
     * @param page            the page number to retrieve
     * @param size            the size of the page to retrieve
     * @param sortBy          the sorting criteria
     * @param startDate       the start date for the transaction filter
     * @param endDate         the end date for the transaction filter
     * @param minAmount       the minimum amount for the transaction filter
     * @param maxAmount       the maximum amount for the transaction filter
     * @param transactionType the type of transactions to retrieve
     * @return a page of transactions for the specified account
     */
    @Override
    public Page<TransactionDTO> getTransactionsForAccount(Integer accountId, int page, int size, String sortBy,
                                                          LocalDateTime startDate, LocalDateTime endDate,
                                                          Double minAmount, Double maxAmount,
                                                          TransactionType transactionType) {
        Sort sort = parseSort(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Timestamp startTimestamp = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp endTimestamp = endDate != null ? Timestamp.valueOf(endDate) : null;


        Page<Transaction> transactions = transactionRepository.findAllWithFilters(accountId, startTimestamp,
                endTimestamp,
                minAmount, maxAmount, transactionType,
                pageRequest);
        return transactions.map(transactionMapper::transactionToTransactionDTO);
    }

    /**
     * Transfers funds between two accounts with retry mechanism.
     *
     * @param requestDTO the transfer request containing source and target account IDs, and the amount to be transferred
     * @throws AccountNotFoundException if the source or target account is not found
     * @throws TransferFundsException   if the transfer fails after multiple attempts
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transferFunds(TransferFundsDTO requestDTO) {
        try {
            retryUtility.performWithRetry(() -> {
                Account sourceAccount = accountRepository.findByIdWithPessimisticLock(requestDTO.getSourceAccountId())
                        .orElseThrow(() -> new AccountNotFoundException("No account found with ID: " + requestDTO.getSourceAccountId()));
                Account targetAccount = accountRepository.findByIdWithPessimisticLock(requestDTO.getTargetAccountId())
                        .orElseThrow(() -> new AccountNotFoundException("No account found with ID: " + requestDTO.getTargetAccountId()));
                transactionOperations.processTransfer(sourceAccount, targetAccount, requestDTO.getAmount());
            }, 3);
        } catch (AccountNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TransferFundsException("Failed to transfer funds after multiple attempts", ex);
        }
    }

    /**
     * Retrieves a page of all transactions based on various filters.
     *
     * @param page            the page number to retrieve
     * @param size            the size of the page to retrieve
     * @param sortBy          the sorting criteria
     * @param startDate       the start date for the transaction filter
     * @param endDate         the end date for the transaction filter
     * @param minAmount       the minimum amount for the transaction filter
     * @param maxAmount       the maximum amount for the transaction filter
     * @param transactionType the type of transactions to retrieve
     * @param lastFour        the last four digits of the account number for filtering
     * @return a page of all transactions based on the specified filters
     */
    @Override
    public Page<TransactionDTO> getAllTransactions(int page, int size, String sortBy, LocalDateTime startDate,
                                                   LocalDateTime endDate,
                                                   Double minAmount, Double maxAmount,
                                                   TransactionType transactionType, String lastFour) {
        Sort sort = parseSort(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        validateInputs(minAmount, maxAmount, startDate, endDate);
        Timestamp startTimestamp = startDate != null ? Timestamp.valueOf(startDate) : null;
        Timestamp endTimestamp = endDate != null ? Timestamp.valueOf(endDate) : null;

        Page<Transaction> transactions = transactionRepository.findAllWithFiltersAdmin(startTimestamp,
                endTimestamp,
                minAmount,
                maxAmount,
                transactionType,
                lastFour,
                pageRequest);

        return transactions.map(transactionMapper::transactionToTransactionDTO);
    }

    /**
     * Retrieves all transactions for a specific account.
     *
     * @param accountId the account ID to retrieve transactions for
     * @return a list of all transactions for the specified account
     */
    @Override
    public List<TransactionDTO> getAllTransactionsForAccount(Integer accountId) {
        return transactionRepository.findAllByAccountAccountIdOrderByDateTimeAsc(accountId)
                .stream()
                .map(transactionMapper::transactionToTransactionDTO)
                .toList();
    }

    /**
     * Deposits funds into an account with retry logic.
     *
     * @param requestDTO the details of the deposit
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void depositFunds(DepositFundsDTO requestDTO) {
        try {
            Account account = retryUtility.performWithRetry(() -> {
                Account acc = accountRepository.findByIdWithPessimisticLock(requestDTO.getAccountId())
                        .orElseThrow(() -> new AccountNotFoundException("No account found with ID: " + requestDTO.getAccountId()));
                transactionOperations.processDeposit(acc, requestDTO.getAmount());
                return acc;
            }, 3);
        } catch (AccountNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DepositFundsException("Failed to deposit funds after multiple attempts", ex);
        }
    }


    /**
     * Makes a payment between accounts with retry logic.
     *
     * @param requestDTO the details of the payment
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void makePayment(PaymentDTO requestDTO) {
        try {
            retryUtility.performWithRetry(() -> {
                Account sourceAccount = accountRepository.findByIdWithPessimisticLock(requestDTO.getSourceAccountId())
                        .orElseThrow(() -> new AccountNotFoundException("No account found with ID: " + requestDTO.getSourceAccountId()));
                Account destinationAccount =
                        accountRepository.findByIdWithPessimisticLock(requestDTO.getDestinationAccountId())
                                .orElseThrow(() -> new AccountNotFoundException("No account found with ID: " + requestDTO.getDestinationAccountId()));
                transactionOperations.processPayment(sourceAccount, destinationAccount, requestDTO.getAmount());
            }, 3);
        } catch (AccountNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new PaymentException("Failed to make payment after multiple attempts", ex);
        }
    }

    /**
     * Reverses a transaction with retry logic.
     *
     * @param transactionId the ID of the transaction to reverse
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void reverseTransaction(Integer transactionId) {
        try {
            retryUtility.performWithRetry(() -> {
                transactionOperations.reverseTransaction(transactionId);
            }, 3);
        } catch (Exception ex) {
            throw new ReverseTransactionException("Failed to reverse transaction after multiple attempts", ex);
        }
    }


    private Sort parseSort(String sortBy) {
        if (StringUtils.isEmpty(sortBy)) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = new ArrayList<>();
        String[] sortCriteria = sortBy.split(",");
        for (int i = 0; i < sortCriteria.length; i += 2) {
            String property = sortCriteria[i];
            Sort.Direction direction = (i + 1) < sortCriteria.length ?
                    Sort.Direction.fromString(sortCriteria[i + 1]) :
                    Sort.DEFAULT_DIRECTION;
            orders.add(new Sort.Order(direction, property));
        }
        return Sort.by(orders);
    }

    /**
     * Validates the input parameters for transactions.
     *
     * @param minAmount the minimum transaction amount
     * @param maxAmount the maximum transaction amount
     * @param startDate the start date for the transaction filter
     * @param endDate   the end date for the transaction filter
     * @throws IllegalArgumentException if validation fails
     */
    private void validateInputs(Double minAmount, Double maxAmount, LocalDateTime startDate, LocalDateTime endDate) {
        if (minAmount != null && maxAmount != null && minAmount > maxAmount) {
            throw new IllegalArgumentException("Minimum balance cannot be greater than maximum balance.");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
    }

    /**
     * Exports transactions to the specified format.
     *
     * @param accountId the account ID to export transactions for
     * @param startDate the start date for the transaction export
     * @param endDate   the end date for the transaction export
     * @param format    the format to export the transactions to (csv, pdf, xlsx)
     * @return a byte array representing the exported transactions
     * @throws IOException if an I/O error occurs during export
     */
    @Override
    public byte[] exportTransactions(Integer accountId, LocalDateTime startDate, LocalDateTime endDate,
                                     String format) throws IOException {
        List<TransactionDTO> transactions = transactionRepository.findAllByAccountAndDateRange(accountId, startDate,
                        endDate)
                .stream()
                .map(transactionMapper::transactionToTransactionDTO)
                .toList();
        Optional<Account> account = accountRepository.findById(accountId);
        AccountType accountType = account.map(Account::getAccountType).orElse(null);
        AccountProgram accountProgram = account.map(Account::getAccountProgram).orElse(null);


        if (transactions.isEmpty()) {
            return createEmptyStatement(accountId, accountType, accountProgram, format);
        }
        try {
            switch (format.toLowerCase()) {
                case "csv" -> {
                    return FileExportUtils.exportToCSV(transactions);
                }
                case "pdf" -> {
                    return FileExportUtils.exportToPDF(transactions, accountType, accountProgram);
                }
                case "xlsx" -> {
                    return FileExportUtils.exportToExcel(transactions);
                }
                default -> {
                    throw new IllegalArgumentException("Unsupported format: " + format);
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }

    private byte[] createEmptyStatement(Integer accountId, AccountType accountType, AccountProgram accountProgram,
                                        String format) throws IOException {
        List<TransactionDTO> emptyTransactions = Collections.emptyList();
        switch (format.toLowerCase()) {
            case "csv" -> {
                return FileExportUtils.exportToCSV(emptyTransactions);
            }
            case "pdf" -> {
                return FileExportUtils.exportToPDF(emptyTransactions, accountType, accountProgram);
            }
            case "xlsx" -> {
                return FileExportUtils.exportToExcel(emptyTransactions);
            }
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}