package com.secure_sentinel.accounts.accounts_service.service;

import com.secure_sentinel.accounts.accounts_service.dao.AccountRepository;
import com.secure_sentinel.accounts.accounts_service.dao.TransactionRepository;
import com.secure_sentinel.accounts.accounts_service.exceptions.InsufficientFundsException;
import com.secure_sentinel.accounts.accounts_service.model.Account;
import com.secure_sentinel.accounts.accounts_service.model.Transaction;
import com.secure_sentinel.accounts.accounts_service.model.enums.AccountType;
import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TransactionOperations.class})
@ExtendWith(SpringExtension.class)
class TransactionOperationsTest {
    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private TransactionOperations transactionOperations;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    void processTransfer_shouldWithdrawAndDepositFunds() {
        // Arrange
        Account sourceAccount = new Account();
        sourceAccount.setAccountId(1);
        sourceAccount.setBalance(100.0);
        sourceAccount.setAccountNumber("123456789");

        Account destinationAccount = new Account();
        destinationAccount.setAccountId(2);
        destinationAccount.setBalance(50.0);
        destinationAccount.setAccountNumber("987654321");

        double transferAmount = 20.0;

        // Mocking the save operation to just return the account
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        transactionOperations.processTransfer(sourceAccount, destinationAccount, transferAmount);

        // Assert
        // Verify account balances are updated
        assertEquals(80.0, sourceAccount.getBalance());
        assertEquals(70.0, destinationAccount.getBalance());

        // Verify that transactions are saved for both withdrawal and deposit
        verify(transactionRepository, times(2)).save(any(Transaction.class));

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(2)).save(transactionCaptor.capture());
        List<Transaction> capturedTransactions = transactionCaptor.getAllValues();

        Transaction withdrawalTransaction = capturedTransactions.get(0);
        assertEquals(TransactionType.TRANSFER, withdrawalTransaction.getTransactionType());
        assertEquals(withdrawalTransaction.getAmount(), transferAmount);
        assertTrue(withdrawalTransaction.getIsCredit());

        Transaction depositTransaction = capturedTransactions.get(1);
        assertEquals(TransactionType.TRANSFER, depositTransaction.getTransactionType());
        assertEquals(depositTransaction.getAmount(), transferAmount);
        assertFalse(depositTransaction.getIsCredit());
    }


    @Test
    void testProcessDeposit2() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);

        Account account3 = new Account();
        account3.setAccountId(1);
        account3.setAccountNumber("42");
        account3.setAccountType(AccountType.CHECKING);
        account3.setAmountOwed(10.0d);
        account3.setBalance(10.0d);
        account3.setCreatedAt(mock(Timestamp.class));
        account3.setCreditLimit(10.0d);
        account3.setIsActive(true);
        account3.setUpdatedAt(mock(Timestamp.class));
        account3.setUserId(1);

        // Act
        transactionOperations.processDeposit(account3, 10.0d, "The characteristics of someone or something",
                TransactionType.DEPOSIT);

        // Assert
        verify(accountRepository).save(Mockito.<Account>any());
        verify(transactionRepository).save(Mockito.<Transaction>any());
        assertEquals(20.0d, account3.getBalance().doubleValue());
    }


    @Test
    void testProcessWithdrawal() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);
        Account account3 = mock(Account.class);
        when(account3.getBalance()).thenReturn(10.0d);
        when(account3.getAccountNumber()).thenReturn("xxxx-4444");
        doNothing().when(account3).setAccountId(Mockito.<Integer>any());
        doNothing().when(account3).setAccountNumber(Mockito.<String>any());
        doNothing().when(account3).setAccountType(Mockito.<AccountType>any());
        doNothing().when(account3).setAmountOwed(Mockito.<Double>any());
        doNothing().when(account3).setBalance(Mockito.<Double>any());
        doNothing().when(account3).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(account3).setCreditLimit(Mockito.<Double>any());
        doNothing().when(account3).setIsActive(Mockito.<Boolean>any());
        doNothing().when(account3).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(account3).setUserId(Mockito.<Integer>any());
        account3.setAccountId(1);
        account3.setAccountNumber("42");
        account3.setAccountType(AccountType.CHECKING);
        account3.setAmountOwed(10.0d);
        account3.setBalance(10.0d);
        account3.setCreatedAt(mock(Timestamp.class));
        account3.setCreditLimit(10.0d);
        account3.setIsActive(true);
        account3.setUpdatedAt(mock(Timestamp.class));
        account3.setUserId(1);

        // Act
        transactionOperations.processWithdrawal(account3, 10.0d);

        // Assert
        verify(account3).getAccountNumber();
        verify(account3, atLeast(1)).getBalance();
        verify(account3).setAccountId(Mockito.<Integer>any());
        verify(account3).setAccountNumber(Mockito.<String>any());
        verify(account3).setAccountType(Mockito.<AccountType>any());
        verify(account3).setAmountOwed(Mockito.<Double>any());
        verify(account3, atLeast(1)).setBalance(Mockito.<Double>any());
        verify(account3).setCreatedAt(Mockito.<Timestamp>any());
        verify(account3).setCreditLimit(Mockito.<Double>any());
        verify(account3).setIsActive(Mockito.<Boolean>any());
        verify(account3).setUpdatedAt(Mockito.<Timestamp>any());
        verify(account3).setUserId(Mockito.<Integer>any());
        verify(accountRepository).save(Mockito.<Account>any());
        verify(transactionRepository).save(Mockito.<Transaction>any());
    }


    @Test
    void testProcessWithdrawal2() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);

        Account account3 = new Account();
        account3.setAccountId(1);
        account3.setAccountNumber("42");
        account3.setAccountType(AccountType.CHECKING);
        account3.setAmountOwed(10.0d);
        account3.setBalance(10.0d);
        account3.setCreatedAt(mock(Timestamp.class));
        account3.setCreditLimit(10.0d);
        account3.setIsActive(true);
        account3.setUpdatedAt(mock(Timestamp.class));
        account3.setUserId(1);

        // Act
        transactionOperations.processWithdrawal(account3, 10.0d, "The characteristics of someone or something",
                TransactionType.DEPOSIT);

        // Assert
        verify(accountRepository).save(Mockito.<Account>any());
        verify(transactionRepository).save(Mockito.<Transaction>any());
        assertEquals(0.0d, account3.getBalance().doubleValue());
    }

    @Test
    void testProcessTransfer() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);
        TransactionOperations transactionOperations = new TransactionOperations(accountRepository,
                transactionRepository);
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getBalance()).thenReturn(10.0d);
        when(sourceAccount.getAccountNumber()).thenReturn("xxxx-4444");
        doNothing().when(sourceAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(sourceAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(sourceAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(sourceAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(sourceAccount).setBalance(Mockito.<Double>any());
        doNothing().when(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(sourceAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setUserId(Mockito.<Integer>any());
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);
        Account destinationAccount = mock(Account.class);
        when(destinationAccount.getBalance()).thenReturn(10.0d);
        when(destinationAccount.getAccountNumber())
                .thenReturn("xxxx-4444");
        doNothing().when(destinationAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(destinationAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(destinationAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(destinationAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(destinationAccount).setBalance(Mockito.<Double>any());
        doNothing().when(destinationAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(destinationAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(destinationAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(destinationAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(destinationAccount).setUserId(Mockito.<Integer>any());
        destinationAccount.setAccountId(1);
        destinationAccount.setAccountNumber("42");
        destinationAccount.setAccountType(AccountType.CHECKING);
        destinationAccount.setAmountOwed(10.0d);
        destinationAccount.setBalance(10.0d);
        destinationAccount.setCreatedAt(mock(Timestamp.class));
        destinationAccount.setCreditLimit(10.0d);
        destinationAccount.setIsActive(true);
        destinationAccount.setUpdatedAt(mock(Timestamp.class));
        destinationAccount.setUserId(1);

        // Act
        transactionOperations.processTransfer(sourceAccount, destinationAccount, 10.0d);

        // Assert
        verify(sourceAccount).getAccountNumber();
        verify(destinationAccount).getAccountNumber();
        verify(destinationAccount, atLeast(1)).getBalance();
        verify(sourceAccount, atLeast(1)).getBalance();
        verify(sourceAccount).setAccountId(Mockito.<Integer>any());
        verify(destinationAccount).setAccountId(Mockito.<Integer>any());
        verify(sourceAccount).setAccountNumber(Mockito.<String>any());
        verify(destinationAccount).setAccountNumber(Mockito.<String>any());
        verify(sourceAccount).setAccountType(Mockito.<AccountType>any());
        verify(destinationAccount).setAccountType(Mockito.<AccountType>any());
        verify(sourceAccount).setAmountOwed(Mockito.<Double>any());
        verify(destinationAccount).setAmountOwed(Mockito.<Double>any());
        verify(sourceAccount, atLeast(1)).setBalance(Mockito.<Double>any());
        verify(destinationAccount, atLeast(1)).setBalance(Mockito.<Double>any());
        verify(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(destinationAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setCreditLimit(Mockito.<Double>any());
        verify(destinationAccount).setCreditLimit(Mockito.<Double>any());
        verify(sourceAccount).setIsActive(Mockito.<Boolean>any());
        verify(destinationAccount).setIsActive(Mockito.<Boolean>any());
        verify(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(destinationAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setUserId(Mockito.<Integer>any());
        verify(destinationAccount).setUserId(Mockito.<Integer>any());
        verify(accountRepository, atLeast(1)).save(Mockito.<Account>any());
        verify(transactionRepository, atLeast(1)).save(Mockito.<Transaction>any());
    }


    @Test
    void testProcessPayment() {
        // Arrange
        Account sourceAccount = new Account();
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);

        Account destinationAccount = new Account();
        destinationAccount.setAccountId(1);
        destinationAccount.setAccountNumber("42");
        destinationAccount.setAccountType(AccountType.CHECKING);
        destinationAccount.setAmountOwed(10.0d);
        destinationAccount.setBalance(10.0d);
        destinationAccount.setCreatedAt(mock(Timestamp.class));
        destinationAccount.setCreditLimit(10.0d);
        destinationAccount.setIsActive(true);
        destinationAccount.setUpdatedAt(mock(Timestamp.class));
        destinationAccount.setUserId(1);

        // Act and Assert
        assertThrows(UnsupportedOperationException.class,
                () -> transactionOperations.processPayment(sourceAccount, destinationAccount, 10.0d));
    }

    /**
     * Method under test:
     * {@link TransactionOperations#processPayment(Account, Account, double)}
     */
    @Test
    void testProcessPayment2() {
        // Arrange
        Account sourceAccount = mock(Account.class);
        doNothing().when(sourceAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(sourceAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(sourceAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(sourceAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(sourceAccount).setBalance(Mockito.<Double>any());
        doNothing().when(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(sourceAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setUserId(Mockito.<Integer>any());
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);

        Account destinationAccount = new Account();
        destinationAccount.setAccountId(1);
        destinationAccount.setAccountNumber("42");
        destinationAccount.setAccountType(AccountType.CHECKING);
        destinationAccount.setAmountOwed(10.0d);
        destinationAccount.setBalance(10.0d);
        destinationAccount.setCreatedAt(mock(Timestamp.class));
        destinationAccount.setCreditLimit(10.0d);
        destinationAccount.setIsActive(true);
        destinationAccount.setUpdatedAt(mock(Timestamp.class));
        destinationAccount.setUserId(1);

        // Act and Assert
        assertThrows(UnsupportedOperationException.class,
                () -> transactionOperations.processPayment(sourceAccount, destinationAccount, 10.0d));
        verify(sourceAccount).setAccountId(Mockito.<Integer>any());
        verify(sourceAccount).setAccountNumber(Mockito.<String>any());
        verify(sourceAccount).setAccountType(Mockito.<AccountType>any());
        verify(sourceAccount).setAmountOwed(Mockito.<Double>any());
        verify(sourceAccount).setBalance(Mockito.<Double>any());
        verify(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setCreditLimit(Mockito.<Double>any());
        verify(sourceAccount).setIsActive(Mockito.<Boolean>any());
        verify(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setUserId(Mockito.<Integer>any());
    }

    /**
     * Method under test:
     * {@link TransactionOperations#processLoanPayment(Account, Account, double)}
     */
    @Test
    void testProcessLoanPayment() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);
        TransactionOperations transactionOperations = new TransactionOperations(accountRepository,
                transactionRepository);
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getBalance()).thenReturn(10.0d);
        when(sourceAccount.getAccountNumber()).thenReturn("xxxx-4444");
        doNothing().when(sourceAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(sourceAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(sourceAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(sourceAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(sourceAccount).setBalance(Mockito.<Double>any());
        doNothing().when(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(sourceAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setUserId(Mockito.<Integer>any());
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);
        Account loanAccount = mock(Account.class);
        when(loanAccount.getBalance()).thenReturn(10.0d);
        when(loanAccount.getAccountNumber()).thenReturn("xxxx-4444");
        doNothing().when(loanAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(loanAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(loanAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(loanAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(loanAccount).setBalance(Mockito.<Double>any());
        doNothing().when(loanAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(loanAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(loanAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(loanAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(loanAccount).setUserId(Mockito.<Integer>any());
        loanAccount.setAccountId(1);
        loanAccount.setAccountNumber("42");
        loanAccount.setAccountType(AccountType.CHECKING);
        loanAccount.setAmountOwed(10.0d);
        loanAccount.setBalance(10.0d);
        loanAccount.setCreatedAt(mock(Timestamp.class));
        loanAccount.setCreditLimit(10.0d);
        loanAccount.setIsActive(true);
        loanAccount.setUpdatedAt(mock(Timestamp.class));
        loanAccount.setUserId(1);

        // Act
        transactionOperations.processLoanPayment(sourceAccount, loanAccount, 10.0d);

        // Assert
        verify(sourceAccount).getAccountNumber();
        verify(loanAccount).getAccountNumber();
        verify(loanAccount, atLeast(1)).getBalance();
        verify(sourceAccount, atLeast(1)).getBalance();
        verify(sourceAccount).setAccountId(Mockito.<Integer>any());
        verify(loanAccount).setAccountId(Mockito.<Integer>any());
        verify(sourceAccount).setAccountNumber(Mockito.<String>any());
        verify(loanAccount).setAccountNumber(Mockito.<String>any());
        verify(sourceAccount).setAccountType(Mockito.<AccountType>any());
        verify(loanAccount).setAccountType(Mockito.<AccountType>any());
        verify(sourceAccount).setAmountOwed(Mockito.<Double>any());
        verify(loanAccount).setAmountOwed(Mockito.<Double>any());
        verify(sourceAccount, atLeast(1)).setBalance(Mockito.<Double>any());
        verify(loanAccount, atLeast(1)).setBalance(Mockito.<Double>any());
        verify(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(loanAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setCreditLimit(Mockito.<Double>any());
        verify(loanAccount).setCreditLimit(Mockito.<Double>any());
        verify(sourceAccount).setIsActive(Mockito.<Boolean>any());
        verify(loanAccount).setIsActive(Mockito.<Boolean>any());
        verify(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(loanAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setUserId(Mockito.<Integer>any());
        verify(loanAccount).setUserId(Mockito.<Integer>any());
        verify(accountRepository, atLeast(1)).save(Mockito.<Account>any());
        verify(transactionRepository, atLeast(1)).save(Mockito.<Transaction>any());
    }

    @Test
    void testProcessLoanPayment2() {
        // Arrange
        TransactionOperations transactionOperations = new TransactionOperations(mock(AccountRepository.class),
                mock(TransactionRepository.class));
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAccountType()).thenReturn(AccountType.CHECKING);
        when(sourceAccount.getBalance()).thenReturn(1.0d);
        when(sourceAccount.getAccountNumber()).thenReturn("com.secure_sentinel.accounts.accounts_service.model" +
                ".Account");
        doNothing().when(sourceAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(sourceAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(sourceAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(sourceAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(sourceAccount).setBalance(Mockito.<Double>any());
        doNothing().when(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(sourceAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setUserId(Mockito.<Integer>any());
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);
        Account loanAccount = mock(Account.class);
        when(loanAccount.getBalance()).thenReturn(10.0d);
        when(loanAccount.getAccountNumber()).thenReturn("com.secure_sentinel.accounts.accounts_service.model.Account");
        doNothing().when(loanAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(loanAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(loanAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(loanAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(loanAccount).setBalance(Mockito.<Double>any());
        doNothing().when(loanAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(loanAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(loanAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(loanAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(loanAccount).setUserId(Mockito.<Integer>any());
        loanAccount.setAccountId(1);
        loanAccount.setAccountNumber("42");
        loanAccount.setAccountType(AccountType.CHECKING);
        loanAccount.setAmountOwed(10.0d);
        loanAccount.setBalance(10.0d);
        loanAccount.setCreatedAt(mock(Timestamp.class));
        loanAccount.setCreditLimit(10.0d);
        loanAccount.setIsActive(true);
        loanAccount.setUpdatedAt(mock(Timestamp.class));
        loanAccount.setUserId(1);

        // Act and Assert
        assertThrows(InsufficientFundsException.class,
                () -> transactionOperations.processLoanPayment(sourceAccount, loanAccount, 10.0d));
        verify(sourceAccount).getAccountNumber();
        verify(loanAccount).getAccountNumber();
        verify(sourceAccount).getAccountType();
        verify(sourceAccount, atLeast(1)).getBalance();
        verify(sourceAccount).setAccountId(Mockito.<Integer>any());
        verify(loanAccount).setAccountId(Mockito.<Integer>any());
        verify(sourceAccount).setAccountNumber(Mockito.<String>any());
        verify(loanAccount).setAccountNumber(Mockito.<String>any());
        verify(sourceAccount).setAccountType(Mockito.<AccountType>any());
        verify(loanAccount).setAccountType(Mockito.<AccountType>any());
        verify(sourceAccount).setAmountOwed(Mockito.<Double>any());
        verify(loanAccount).setAmountOwed(Mockito.<Double>any());
        verify(sourceAccount).setBalance(Mockito.<Double>any());
        verify(loanAccount).setBalance(Mockito.<Double>any());
        verify(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(loanAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setCreditLimit(Mockito.<Double>any());
        verify(loanAccount).setCreditLimit(Mockito.<Double>any());
        verify(sourceAccount).setIsActive(Mockito.<Boolean>any());
        verify(loanAccount).setIsActive(Mockito.<Boolean>any());
        verify(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(loanAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setUserId(Mockito.<Integer>any());
        verify(loanAccount).setUserId(Mockito.<Integer>any());
    }

    @Test
    void testDeductFromLoanAccount() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);

        Account loanAccount = new Account();
        loanAccount.setAccountId(1);
        loanAccount.setAccountNumber("42");
        loanAccount.setAccountType(AccountType.CHECKING);
        loanAccount.setAmountOwed(10.0d);
        loanAccount.setBalance(10.0d);
        loanAccount.setCreatedAt(mock(Timestamp.class));
        loanAccount.setCreditLimit(10.0d);
        loanAccount.setIsActive(true);
        loanAccount.setUpdatedAt(mock(Timestamp.class));
        loanAccount.setUserId(1);

        // Act
        transactionOperations.deductFromLoanAccount(loanAccount, 10.0d, "The characteristics of someone or something");

        // Assert
        verify(accountRepository).save(Mockito.<Account>any());
        verify(transactionRepository).save(Mockito.<Transaction>any());
        assertEquals(0.0d, loanAccount.getBalance().doubleValue());
    }

    /**
     * Method under test:
     * {@link TransactionOperations#processCreditPayment(Account, Account, double)}
     */
    @Test
    void testProcessCreditPayment() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);
        TransactionOperations transactionOperations = new TransactionOperations(accountRepository,
                transactionRepository);
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getBalance()).thenReturn(10.0d);
        when(sourceAccount.getAccountNumber()).thenReturn("com.secure_sentinel.accounts.accounts_service.model" +
                ".Account");
        doNothing().when(sourceAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(sourceAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(sourceAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(sourceAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(sourceAccount).setBalance(Mockito.<Double>any());
        doNothing().when(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(sourceAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setUserId(Mockito.<Integer>any());
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);
        Account creditAccount = mock(Account.class);
        when(creditAccount.getAmountOwed()).thenReturn(10.0d);
        when(creditAccount.getBalance()).thenReturn(10.0d);
        when(creditAccount.getAccountNumber()).thenReturn("com.secure_sentinel.accounts.accounts_service.model" +
                ".Account");
        doNothing().when(creditAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(creditAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(creditAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(creditAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(creditAccount).setBalance(Mockito.<Double>any());
        doNothing().when(creditAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(creditAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(creditAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(creditAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(creditAccount).setUserId(Mockito.<Integer>any());
        creditAccount.setAccountId(1);
        creditAccount.setAccountNumber("42");
        creditAccount.setAccountType(AccountType.CHECKING);
        creditAccount.setAmountOwed(10.0d);
        creditAccount.setBalance(10.0d);
        creditAccount.setCreatedAt(mock(Timestamp.class));
        creditAccount.setCreditLimit(10.0d);
        creditAccount.setIsActive(true);
        creditAccount.setUpdatedAt(mock(Timestamp.class));
        creditAccount.setUserId(1);

        // Act
        transactionOperations.processCreditPayment(sourceAccount, creditAccount, 10.0d);

        // Assert
        verify(sourceAccount).getAccountNumber();
        verify(creditAccount).getAccountNumber();
        verify(creditAccount).getAmountOwed();
        verify(creditAccount, atLeast(1)).getBalance();
        verify(sourceAccount, atLeast(1)).getBalance();
        verify(sourceAccount).setAccountId(Mockito.<Integer>any());
        verify(creditAccount).setAccountId(Mockito.<Integer>any());
        verify(sourceAccount).setAccountNumber(Mockito.<String>any());
        verify(creditAccount).setAccountNumber(Mockito.<String>any());
        verify(sourceAccount).setAccountType(Mockito.<AccountType>any());
        verify(creditAccount).setAccountType(Mockito.<AccountType>any());
        verify(sourceAccount).setAmountOwed(Mockito.<Double>any());
        verify(creditAccount, atLeast(1)).setAmountOwed(Mockito.<Double>any());
        verify(sourceAccount, atLeast(1)).setBalance(Mockito.<Double>any());
        verify(creditAccount, atLeast(1)).setBalance(Mockito.<Double>any());
        verify(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(creditAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setCreditLimit(Mockito.<Double>any());
        verify(creditAccount).setCreditLimit(Mockito.<Double>any());
        verify(sourceAccount).setIsActive(Mockito.<Boolean>any());
        verify(creditAccount).setIsActive(Mockito.<Boolean>any());
        verify(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(creditAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setUserId(Mockito.<Integer>any());
        verify(creditAccount).setUserId(Mockito.<Integer>any());
        verify(accountRepository, atLeast(1)).save(Mockito.<Account>any());
        verify(transactionRepository, atLeast(1)).save(Mockito.<Transaction>any());
    }

    /**
     * Method under test:
     * {@link TransactionOperations#processCreditPayment(Account, Account, double)}
     */
    @Test
    void testProcessCreditPayment2() {
        // Arrange
        TransactionOperations transactionOperations = new TransactionOperations(mock(AccountRepository.class),
                mock(TransactionRepository.class));
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAccountType()).thenReturn(AccountType.CHECKING);
        when(sourceAccount.getBalance()).thenReturn(1.0d);
        when(sourceAccount.getAccountNumber()).thenReturn("com.secure_sentinel.accounts.accounts_service.model" +
                ".Account");
        doNothing().when(sourceAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(sourceAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(sourceAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(sourceAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(sourceAccount).setBalance(Mockito.<Double>any());
        doNothing().when(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(sourceAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setUserId(Mockito.<Integer>any());
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);
        Account creditAccount = mock(Account.class);
        when(creditAccount.getBalance()).thenReturn(10.0d);
        when(creditAccount.getAccountNumber()).thenReturn("com.secure_sentinel.accounts.accounts_service.model" +
                ".Account");
        doNothing().when(creditAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(creditAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(creditAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(creditAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(creditAccount).setBalance(Mockito.<Double>any());
        doNothing().when(creditAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(creditAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(creditAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(creditAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(creditAccount).setUserId(Mockito.<Integer>any());
        creditAccount.setAccountId(1);
        creditAccount.setAccountNumber("42");
        creditAccount.setAccountType(AccountType.CHECKING);
        creditAccount.setAmountOwed(10.0d);
        creditAccount.setBalance(10.0d);
        creditAccount.setCreatedAt(mock(Timestamp.class));
        creditAccount.setCreditLimit(10.0d);
        creditAccount.setIsActive(true);
        creditAccount.setUpdatedAt(mock(Timestamp.class));
        creditAccount.setUserId(1);

        // Act and Assert
        assertThrows(InsufficientFundsException.class,
                () -> transactionOperations.processCreditPayment(sourceAccount, creditAccount, 10.0d));
        verify(sourceAccount).getAccountNumber();
        verify(creditAccount).getAccountNumber();
        verify(sourceAccount).getAccountType();
        verify(sourceAccount, atLeast(1)).getBalance();
        verify(sourceAccount).setAccountId(Mockito.<Integer>any());
        verify(creditAccount).setAccountId(Mockito.<Integer>any());
        verify(sourceAccount).setAccountNumber(Mockito.<String>any());
        verify(creditAccount).setAccountNumber(Mockito.<String>any());
        verify(sourceAccount).setAccountType(Mockito.<AccountType>any());
        verify(creditAccount).setAccountType(Mockito.<AccountType>any());
        verify(sourceAccount).setAmountOwed(Mockito.<Double>any());
        verify(creditAccount).setAmountOwed(Mockito.<Double>any());
        verify(sourceAccount).setBalance(Mockito.<Double>any());
        verify(creditAccount).setBalance(Mockito.<Double>any());
        verify(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(creditAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setCreditLimit(Mockito.<Double>any());
        verify(creditAccount).setCreditLimit(Mockito.<Double>any());
        verify(sourceAccount).setIsActive(Mockito.<Boolean>any());
        verify(creditAccount).setIsActive(Mockito.<Boolean>any());
        verify(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(creditAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setUserId(Mockito.<Integer>any());
        verify(creditAccount).setUserId(Mockito.<Integer>any());
    }

    /**
     * Method under test:
     * {@link TransactionOperations#creditPaymentToCreditAccount(Account, double, String)}
     */
    @Test
    void testCreditPaymentToCreditAccount() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);

        Account creditAccount = new Account();
        creditAccount.setAccountId(1);
        creditAccount.setAccountNumber("42");
        creditAccount.setAccountType(AccountType.CHECKING);
        creditAccount.setAmountOwed(10.0d);
        creditAccount.setBalance(10.0d);
        creditAccount.setCreatedAt(mock(Timestamp.class));
        creditAccount.setCreditLimit(10.0d);
        creditAccount.setIsActive(true);
        creditAccount.setUpdatedAt(mock(Timestamp.class));
        creditAccount.setUserId(1);

        // Act
        transactionOperations.creditPaymentToCreditAccount(creditAccount, 10.0d,
                "The characteristics of someone or something");

        // Assert
        verify(accountRepository).save(Mockito.<Account>any());
        verify(transactionRepository).save(Mockito.<Transaction>any());
        assertEquals(0.0d, creditAccount.getAmountOwed().doubleValue());
        assertEquals(20.0d, creditAccount.getBalance().doubleValue());
    }

    /**
     * Method under test:
     * {@link TransactionOperations#processPaymentWithdrawal(Account, double, String)}
     */
    @Test
    void testProcessPaymentWithdrawal() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction);

        Account sourceAccount = new Account();
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);

        // Act
        transactionOperations.processPaymentWithdrawal(sourceAccount, 10.0d, "The characteristics of someone or " +
                "something");

        // Assert
        verify(accountRepository).save(Mockito.<Account>any());
        verify(transactionRepository).save(Mockito.<Transaction>any());
        assertEquals(0.0d, sourceAccount.getBalance().doubleValue());
    }

    /**
     * Method under test:
     * {@link TransactionOperations#verifyFundsInSourceAccount(Account, double)}
     */
    @Test
    void testVerifyFundsInSourceAccount() {
        // Arrange
        Account sourceAccount = new Account();
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);

        // Act
        transactionOperations.verifyFundsInSourceAccount(sourceAccount, 10.0d);

        // Assert that nothing has changed
        assertEquals("42", sourceAccount.getAccountNumber());
        assertEquals(1, sourceAccount.getAccountId().intValue());
        assertEquals(1, sourceAccount.getUserId().intValue());
        assertEquals(10.0d, sourceAccount.getAmountOwed().doubleValue());
        assertEquals(10.0d, sourceAccount.getBalance().doubleValue());
        assertEquals(10.0d, sourceAccount.getCreditLimit().doubleValue());
        assertEquals(AccountType.CHECKING, sourceAccount.getAccountType());
        assertTrue(sourceAccount.getIsActive());
    }

    /**
     * Method under test:
     * {@link TransactionOperations#verifyFundsInSourceAccount(Account, double)}
     */
    @Test
    void testVerifyFundsInSourceAccount2() {
        // Arrange
        Account sourceAccount = mock(Account.class);
        when(sourceAccount.getAccountType()).thenReturn(AccountType.CHECKING);
        when(sourceAccount.getBalance()).thenReturn(1.0d);
        when(sourceAccount.getAccountNumber()).thenReturn("Insufficient funds in payment source account");
        doNothing().when(sourceAccount).setAccountId(Mockito.<Integer>any());
        doNothing().when(sourceAccount).setAccountNumber(Mockito.<String>any());
        doNothing().when(sourceAccount).setAccountType(Mockito.<AccountType>any());
        doNothing().when(sourceAccount).setAmountOwed(Mockito.<Double>any());
        doNothing().when(sourceAccount).setBalance(Mockito.<Double>any());
        doNothing().when(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setCreditLimit(Mockito.<Double>any());
        doNothing().when(sourceAccount).setIsActive(Mockito.<Boolean>any());
        doNothing().when(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        doNothing().when(sourceAccount).setUserId(Mockito.<Integer>any());
        sourceAccount.setAccountId(1);
        sourceAccount.setAccountNumber("42");
        sourceAccount.setAccountType(AccountType.CHECKING);
        sourceAccount.setAmountOwed(10.0d);
        sourceAccount.setBalance(10.0d);
        sourceAccount.setCreatedAt(mock(Timestamp.class));
        sourceAccount.setCreditLimit(10.0d);
        sourceAccount.setIsActive(true);
        sourceAccount.setUpdatedAt(mock(Timestamp.class));
        sourceAccount.setUserId(1);

        // Act and Assert
        assertThrows(InsufficientFundsException.class,
                () -> transactionOperations.verifyFundsInSourceAccount(sourceAccount, 10.0d));
        verify(sourceAccount).getAccountNumber();
        verify(sourceAccount).getAccountType();
        verify(sourceAccount, atLeast(1)).getBalance();
        verify(sourceAccount).setAccountId(Mockito.<Integer>any());
        verify(sourceAccount).setAccountNumber(Mockito.<String>any());
        verify(sourceAccount).setAccountType(Mockito.<AccountType>any());
        verify(sourceAccount).setAmountOwed(Mockito.<Double>any());
        verify(sourceAccount).setBalance(Mockito.<Double>any());
        verify(sourceAccount).setCreatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setCreditLimit(Mockito.<Double>any());
        verify(sourceAccount).setIsActive(Mockito.<Boolean>any());
        verify(sourceAccount).setUpdatedAt(Mockito.<Timestamp>any());
        verify(sourceAccount).setUserId(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link TransactionOperations#reverseTransaction(Integer)}
     */
    @Test
    void testReverseTransaction() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Transaction transaction = new Transaction();
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        Optional<Transaction> ofResult = Optional.of(transaction);

        Account account3 = new Account();
        account3.setAccountId(1);
        account3.setAccountNumber("42");
        account3.setAccountType(AccountType.CHECKING);
        account3.setAmountOwed(10.0d);
        account3.setBalance(10.0d);
        account3.setCreatedAt(mock(Timestamp.class));
        account3.setCreditLimit(10.0d);
        account3.setIsActive(true);
        account3.setUpdatedAt(mock(Timestamp.class));
        account3.setUserId(1);

        Transaction transaction2 = new Transaction();
        transaction2.setAccount(account3);
        transaction2.setAmount(10.0d);
        transaction2.setClosingBalance(10.0d);
        transaction2.setDateTime(mock(Timestamp.class));
        transaction2.setDescription("The characteristics of someone or something");
        transaction2.setIsCredit(true);
        transaction2.setTransactionId(1);
        transaction2.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction2);
        when(transactionRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        transactionOperations.reverseTransaction(1);

        // Assert
        verify(transactionRepository).findById(Mockito.<Integer>any());
        verify(accountRepository).save(Mockito.<Account>any());
        verify(transactionRepository).save(Mockito.<Transaction>any());
    }

    /**
     * Method under test: {@link TransactionOperations#reverseTransaction(Integer)}
     */
    @Test
    void testReverseTransaction2() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountNumber("42");
        account.setAccountType(AccountType.CHECKING);
        account.setAmountOwed(10.0d);
        account.setBalance(10.0d);
        account.setCreatedAt(mock(Timestamp.class));
        account.setCreditLimit(10.0d);
        account.setIsActive(true);
        account.setUpdatedAt(mock(Timestamp.class));
        account.setUserId(1);
        when(accountRepository.save(Mockito.<Account>any())).thenReturn(account);

        Account account2 = new Account();
        account2.setAccountId(1);
        account2.setAccountNumber("42");
        account2.setAccountType(AccountType.CHECKING);
        account2.setAmountOwed(10.0d);
        account2.setBalance(10.0d);
        account2.setCreatedAt(mock(Timestamp.class));
        account2.setCreditLimit(10.0d);
        account2.setIsActive(true);
        account2.setUpdatedAt(mock(Timestamp.class));
        account2.setUserId(1);

        Account account3 = new Account();
        account3.setAccountId(1);
        account3.setAccountNumber("42");
        account3.setAccountType(AccountType.CHECKING);
        account3.setAmountOwed(10.0d);
        account3.setBalance(10.0d);
        account3.setCreatedAt(mock(Timestamp.class));
        account3.setCreditLimit(10.0d);
        account3.setIsActive(true);
        account3.setUpdatedAt(mock(Timestamp.class));
        account3.setUserId(1);
        Transaction transaction = mock(Transaction.class);
        when(transaction.getTransactionId()).thenThrow(new UnsupportedOperationException("foo"));
        when(transaction.getAccount()).thenReturn(account3);
        when(transaction.getIsCredit()).thenReturn(true);
        when(transaction.getAmount()).thenReturn(10.0d);
        doNothing().when(transaction).setAccount(Mockito.<Account>any());
        doNothing().when(transaction).setAmount(Mockito.<Double>any());
        doNothing().when(transaction).setClosingBalance(Mockito.<Double>any());
        doNothing().when(transaction).setDateTime(Mockito.<Timestamp>any());
        doNothing().when(transaction).setDescription(Mockito.<String>any());
        doNothing().when(transaction).setIsCredit(Mockito.<Boolean>any());
        doNothing().when(transaction).setTransactionId(Mockito.<Integer>any());
        doNothing().when(transaction).setTransactionType(Mockito.<TransactionType>any());
        transaction.setAccount(account2);
        transaction.setAmount(10.0d);
        transaction.setClosingBalance(10.0d);
        transaction.setDateTime(mock(Timestamp.class));
        transaction.setDescription("The characteristics of someone or something");
        transaction.setIsCredit(true);
        transaction.setTransactionId(1);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        Optional<Transaction> ofResult = Optional.of(transaction);

        Account account4 = new Account();
        account4.setAccountId(1);
        account4.setAccountNumber("42");
        account4.setAccountType(AccountType.CHECKING);
        account4.setAmountOwed(10.0d);
        account4.setBalance(10.0d);
        account4.setCreatedAt(mock(Timestamp.class));
        account4.setCreditLimit(10.0d);
        account4.setIsActive(true);
        account4.setUpdatedAt(mock(Timestamp.class));
        account4.setUserId(1);

        Transaction transaction2 = new Transaction();
        transaction2.setAccount(account4);
        transaction2.setAmount(10.0d);
        transaction2.setClosingBalance(10.0d);
        transaction2.setDateTime(mock(Timestamp.class));
        transaction2.setDescription("The characteristics of someone or something");
        transaction2.setIsCredit(true);
        transaction2.setTransactionId(1);
        transaction2.setTransactionType(TransactionType.DEPOSIT);
        when(transactionRepository.save(Mockito.<Transaction>any())).thenReturn(transaction2);
        when(transactionRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(UnsupportedOperationException.class, () -> transactionOperations.reverseTransaction(1));
        verify(transaction).getAccount();
        verify(transaction).getAmount();
        verify(transaction).getIsCredit();
        verify(transaction).getTransactionId();
        verify(transaction).setAccount(Mockito.<Account>any());
        verify(transaction).setAmount(Mockito.<Double>any());
        verify(transaction).setClosingBalance(Mockito.<Double>any());
        verify(transaction).setDateTime(Mockito.<Timestamp>any());
        verify(transaction).setDescription(Mockito.<String>any());
        verify(transaction).setIsCredit(Mockito.<Boolean>any());
        verify(transaction).setTransactionId(Mockito.<Integer>any());
        verify(transaction).setTransactionType(Mockito.<TransactionType>any());
        verify(transactionRepository).findById(Mockito.<Integer>any());
        verify(accountRepository).save(Mockito.<Account>any());
    }
}