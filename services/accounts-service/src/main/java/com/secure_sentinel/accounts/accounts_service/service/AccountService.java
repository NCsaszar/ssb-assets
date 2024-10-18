package com.secure_sentinel.accounts.accounts_service.service;

import com.secure_sentinel.accounts.accounts_service.dto.AccountCreationRequestDTO;
import com.secure_sentinel.accounts.accounts_service.dto.AccountDTO;
import com.secure_sentinel.accounts.accounts_service.dto.AccountWIthTransactions;
import com.secure_sentinel.accounts.accounts_service.dto.DetailedAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
    Page<AccountDTO> getAllAccounts(int page, int size, String sortBy, String accountType,
                                    Double minBalance, Double maxBalance, Boolean isActive,
                                    LocalDateTime startDate, LocalDateTime endDate);

    AccountDTO getAccountByNumber(String accountNumber);

    DetailedAccountDTO getAccountDetails(Integer accountId);

    List<AccountDTO> getAccountsByUserId(Integer userId);

    List<AccountDTO> getActiveAccountsByUserId(Integer userId);

    AccountDTO createAccount(AccountCreationRequestDTO request);

    void deactivateAccount(Integer accountId);

    void activateAccount(Integer accountId);

    void processFile(MultipartFile file) throws IOException;

    List<AccountDTO> getAllAccountsForBatch();

    List<AccountWIthTransactions> getAllAccountsWithTransactions(LocalDateTime startDate, LocalDateTime endDate);
}