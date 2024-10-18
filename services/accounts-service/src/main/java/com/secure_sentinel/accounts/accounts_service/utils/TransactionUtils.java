package com.secure_sentinel.accounts.accounts_service.utils;

import com.secure_sentinel.accounts.accounts_service.dao.TransactionRepository;
import com.secure_sentinel.accounts.accounts_service.model.Account;
import com.secure_sentinel.accounts.accounts_service.model.Transaction;
import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;

import java.util.HashMap;
import java.util.Map;

public class TransactionUtils {

    public static void createAndSaveTransaction(TransactionRepository transactionRepository,
                                                Account account,
                                                double amount,
                                                TransactionType transactionType,
                                                String description, Boolean isCredit) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setDescription(description);
        transaction.setClosingBalance(account.getBalance());
        transaction.setIsCredit(isCredit);
        transactionRepository.save(transaction);
    }

    public static Map<String, Object> buildErrorDetails(Account account, double requiredAmount) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("accountNumber", AccountUtils.maskAccountNumber(account.getAccountNumber()));
        errorDetails.put("accountType", account.getAccountType().getValue());
        errorDetails.put("requiredBalance", requiredAmount);
        errorDetails.put("currentBalance", account.getBalance());
        return errorDetails;
    }
}