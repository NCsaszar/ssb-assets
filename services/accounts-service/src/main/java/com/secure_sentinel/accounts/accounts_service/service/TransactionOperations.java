package com.secure_sentinel.accounts.accounts_service.service;

import com.secure_sentinel.accounts.accounts_service.dao.AccountRepository;
import com.secure_sentinel.accounts.accounts_service.dao.TransactionRepository;
import com.secure_sentinel.accounts.accounts_service.exceptions.InsufficientFundsException;
import com.secure_sentinel.accounts.accounts_service.model.Account;
import com.secure_sentinel.accounts.accounts_service.model.Transaction;
import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import com.secure_sentinel.accounts.accounts_service.utils.AccountUtils;
import com.secure_sentinel.accounts.accounts_service.utils.TransactionUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class TransactionOperations {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    public void processDeposit(Account account, double amount, String description, TransactionType transactionType) {
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
        description = (description == null || description.isEmpty()) ?
                "Deposit into account: " + AccountUtils.maskAccountNumber(account.getAccountNumber()) : description;
        transactionType = transactionType != null ? transactionType : TransactionType.DEPOSIT;
        TransactionUtils.createAndSaveTransaction(transactionRepository, account, amount, transactionType,
                description, true);
    }

    public void processDeposit(Account account, double amount) {
        processDeposit(account, amount, null, TransactionType.DEPOSIT);
    }

    public void processWithdrawal(Account account, double amount, String description, TransactionType transactionType) {
        verifyFundsInSourceAccount(account, amount);
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
        description = (description == null || description.isEmpty()) ?
                "Withdrawal from account: " + AccountUtils.maskAccountNumber(account.getAccountNumber()) : description;
        transactionType = transactionType != null ? transactionType : TransactionType.WITHDRAWAL;
        TransactionUtils.createAndSaveTransaction(transactionRepository, account, amount, transactionType,
                description, false);
    }

    public void processWithdrawal(Account account, double amount) {
        processWithdrawal(account, amount, null, TransactionType.WITHDRAWAL);
    }

    public void processTransfer(Account sourceAccount, Account destinationAccount, double amount) {
        processWithdrawal(sourceAccount, amount,
                "Transfer funds to account: " + AccountUtils.maskAccountNumber(destinationAccount.getAccountNumber())
                , TransactionType.TRANSFER);
        processDeposit(destinationAccount, amount,
                "Transferred funds from account: " + AccountUtils.maskAccountNumber((sourceAccount).getAccountNumber()),
                TransactionType.TRANSFER);
    }

    public void processPayment(Account sourceAccount, Account destinationAccount, double amount) {
        switch (destinationAccount.getAccountType()) {
            case CREDIT:
                processCreditPayment(sourceAccount, destinationAccount, amount);
                break;
            case LOAN:
                processLoanPayment(sourceAccount, destinationAccount, amount);
                break;
            default:
                throw new UnsupportedOperationException("Payment type not supported for account: " + destinationAccount.getAccountType());
        }
    }

    public void processLoanPayment(Account sourceAccount, Account loanAccount, double amount) {
        processPaymentWithdrawal(sourceAccount, amount,
                "Loan payment to account: " + AccountUtils.maskAccountNumber(loanAccount.getAccountNumber()));

        deductFromLoanAccount(loanAccount, amount,
                "Loan payment from account: " + AccountUtils.maskAccountNumber(sourceAccount.getAccountNumber()));
    }

    public void deductFromLoanAccount(Account loanAccount, double amount, String description) {
        loanAccount.setBalance(loanAccount.getBalance() - amount);
        accountRepository.save(loanAccount);
        TransactionUtils.createAndSaveTransaction(transactionRepository, loanAccount, amount,
                TransactionType.PAYMENT, description, false);
    }

    public void processCreditPayment(Account sourceAccount, Account creditAccount, double amount) {
        // Deduct amount from source account and save new balance
        processPaymentWithdrawal(sourceAccount, amount,
                "Credit card payment to account: " + AccountUtils.maskAccountNumber(creditAccount.getAccountNumber()));

        // Add Credit back to credit account reduce balance owed
        creditPaymentToCreditAccount(creditAccount, amount,
                "Credit card payment from account: " + AccountUtils.maskAccountNumber(sourceAccount.getAccountNumber()));
    }

    public void creditPaymentToCreditAccount(Account creditAccount, double amount, String description) {
        creditAccount.setBalance(creditAccount.getBalance() + amount);
        creditAccount.setAmountOwed(creditAccount.getAmountOwed() - amount);
        accountRepository.save(creditAccount);
        TransactionUtils.createAndSaveTransaction(transactionRepository, creditAccount, amount,
                TransactionType.PAYMENT, description, false);
    }

    public void processPaymentWithdrawal(Account sourceAccount, double amount, String description) {
        verifyFundsInSourceAccount(sourceAccount, amount);
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        accountRepository.save(sourceAccount);
        TransactionUtils.createAndSaveTransaction(transactionRepository, sourceAccount, amount,
                TransactionType.PAYMENT, description, true);
    }


    public void verifyFundsInSourceAccount(Account sourceAccount, double amount) {
        if (sourceAccount.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds in payment source account",
                    TransactionUtils.buildErrorDetails(sourceAccount, amount));
        }
    }

    public void reverseTransaction(Integer transactionId) {
        Transaction originalTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));

        double reversalAmount = originalTransaction.getAmount() * (originalTransaction.getIsCredit() ? -1 : 1);

        // Update the account balance
        Account account = originalTransaction.getAccount();
        account.setBalance(account.getBalance() + reversalAmount);
        accountRepository.save(account);

        // Create a reversal transaction
        String description = "Reversal of Transaction ID: " + originalTransaction.getTransactionId();
        TransactionType reversalType = TransactionType.REVERSAL;
        boolean isCredit = !originalTransaction.getIsCredit();

        TransactionUtils.createAndSaveTransaction(transactionRepository, account, Math.abs(reversalAmount),
                reversalType, description, isCredit);
    }
}