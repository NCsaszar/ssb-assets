//package com.secure_sentinel.accounts.accounts_service.service;
//
//import com.secure_sentinel.accounts.accounts_service.dao.AccountRepository;
//import com.secure_sentinel.accounts.accounts_service.dao.TransactionRepository;
//import com.secure_sentinel.accounts.accounts_service.dto.DepositFundsDTO;
//import com.secure_sentinel.accounts.accounts_service.dto.PaymentDTO;
//import com.secure_sentinel.accounts.accounts_service.dto.TransactionDTO;
//import com.secure_sentinel.accounts.accounts_service.dto.TransferFundsDTO;
//import com.secure_sentinel.accounts.accounts_service.exceptions.DepositFundsException;
//import com.secure_sentinel.accounts.accounts_service.exceptions.PaymentException;
//import com.secure_sentinel.accounts.accounts_service.exceptions.ReverseTransactionException;
//import com.secure_sentinel.accounts.accounts_service.exceptions.TransferFundsException;
//import com.secure_sentinel.accounts.accounts_service.mapper.TransactionMapper;
//import com.secure_sentinel.accounts.accounts_service.model.Account;
//import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ContextConfiguration(classes = {TransactionService.class})
//@ExtendWith(SpringExtension.class)
//class TransactionServiceTest {
//    @MockBean
//    private AccountRepository accountRepository;
//
//    @MockBean
//    private RetryUtility retryUtility;
//
//    @MockBean
//    private TransactionMapper transactionMapper;
//
//    @MockBean
//    private TransactionOperations transactionOperations;
//
//    @MockBean
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private TransactionService transactionService;
//
//    @Test
//    void testSuccessfulDepositFunds() {
//        DepositFundsDTO requestDTO = new DepositFundsDTO();
//        requestDTO.setAccountId(1);
//        requestDTO.setAmount(100.0);
//
//        Account account = new Account();
//        account.setAccountId(1);
//        account.setBalance(1000.0);
//
//        when(accountRepository.findByIdWithPessimisticLock(requestDTO.getAccountId())).thenReturn(Optional.of
//        (account));
//
//        doAnswer(invocation -> {
//            Runnable runnable = invocation.getArgument(0);
//            runnable.run();
//            return null;
//        }).when(retryUtility).performWithRetry(any(Runnable.class), anyInt());
//
//        doNothing().when(transactionOperations).processDeposit(account, requestDTO.getAmount());
//
//        transactionService.depositFunds(requestDTO);
//
//        verify(accountRepository, times(1)).findByIdWithPessimisticLock(requestDTO.getAccountId());
//        verify(transactionOperations, times(1)).processDeposit(account, requestDTO.getAmount());
//    }
//
//    @Test
//    void testAccountRepositorySave() {
//        AccountRepository accountRepository = mock(AccountRepository.class);
//        Account account = new Account();
//        account.setAccountId(1);
//        account.setBalance(100.0);
//
//        accountRepository.save(account);
//
//        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
//        verify(accountRepository).save(accountCaptor.capture());
//        Account capturedAccount = accountCaptor.getValue();
//
//        assertEquals(100.0, capturedAccount.getBalance(), "The captured account balance should match the expected " +
//                "value.");
//    }
//
//
//    @Test
//    void testTransferFunds2() {
//        doThrow(new TransferFundsException("Failed to transfer funds", null)).when(retryUtility).performWithRetry
//        (Mockito.<Runnable>any(), anyInt());
//
//        TransferFundsDTO requestDTO = new TransferFundsDTO();
//        requestDTO.setAmount(10.0d);
//        requestDTO.setSourceAccountId(1);
//        requestDTO.setTargetAccountId(1);
//
//        assertThrows(TransferFundsException.class, () -> transactionService.transferFunds(requestDTO));
//        verify(retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//    }
//
//
//    @Test
//    void testGetAllTransactions() {
//        when(transactionRepository.findAllWithFiltersAdmin(Mockito.<Timestamp>any(), Mockito.<Timestamp>any(),
//                Mockito.<Double>any(), Mockito.<Double>any(), Mockito.<TransactionType>any(), Mockito.<String>any(),
//                Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
//        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
//        Page<TransactionDTO> actualAllTransactions = transactionService.getAllTransactions(1, 3, "Sort By", startDate,
//                LocalDate.of(1970, 1, 1).atStartOfDay(), 10.0d, 10.0d, TransactionType.DEPOSIT, "Last Four");
//        verify(transactionRepository).findAllWithFiltersAdmin(Mockito.<Timestamp>any(), Mockito.<Timestamp>any(),
//                Mockito.<Double>any(), Mockito.<Double>any(), Mockito.<TransactionType>any(), Mockito.<String>any(),
//                Mockito.<Pageable>any());
//        assertTrue(actualAllTransactions.toList().isEmpty());
//    }
//
//
//    @Test
//    void testGetAllTransactions2() {
//        LocalDateTime startDate = LocalDate.of(1970, 1, 1).atStartOfDay();
//        assertThrows(IllegalArgumentException.class, () -> transactionService.getAllTransactions(1, 3, "Sort By",
//                startDate,
//                LocalDate.of(1970, 1, 1).atStartOfDay(), 10.0d, 0.5d, TransactionType.DEPOSIT, "Last Four"));
//    }
//
//
//    @Test
//    void testGetAllTransactionsForAccount() {
//        when(transactionRepository.findAllByAccountAccountIdOrderByDateTimeAsc(Mockito.<Integer>any()))
//                .thenReturn(new ArrayList<>());
//        List<TransactionDTO> actualAllTransactionsForAccount = transactionService.getAllTransactionsForAccount(1);
//        verify(transactionRepository).findAllByAccountAccountIdOrderByDateTimeAsc(Mockito.<Integer>any());
//        assertTrue(actualAllTransactionsForAccount.isEmpty());
//    }
//
//
//    @Test
//    void testDepositFunds2() {
//        doThrow(new DepositFundsException("Failed to deposit funds after multiple attempts", null)).when
//        (retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//
//        DepositFundsDTO requestDTO = new DepositFundsDTO();
//        requestDTO.setAccountId(1);
//        requestDTO.setAmount(10.0d);
//
//        assertThrows(DepositFundsException.class, () -> transactionService.depositFunds(requestDTO));
//        verify(retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//    }
//
//
//    @Test
//    void testMakePayment() {
//        doNothing().when(retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//        transactionService.makePayment(new PaymentDTO());
//        verify(retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//    }
//
//    @Test
//    void testMakePayment2() {
//        doThrow(new PaymentException("Failed to make payment", null)).when(retryUtility).performWithRetry(Mockito
//        .<Runnable>any(), anyInt());
//
//        PaymentDTO paymentDTO = new PaymentDTO();
//        paymentDTO.setAmount(10.0d);
//        paymentDTO.setDestinationAccountId(1);
//        paymentDTO.setSourceAccountId(1);
//
//        assertThrows(PaymentException.class, () -> transactionService.makePayment(paymentDTO));
//        verify(retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//    }
//
//
//    @Test
//    void testReverseTransaction() {
//        doNothing().when(retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//        transactionService.reverseTransaction(1);
//        verify(retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//    }
//
//    @Test
//    void testReverseTransaction2() {
//        doThrow(new ReverseTransactionException("Failed to reverse transaction", null)).when(retryUtility)
//        .performWithRetry(Mockito.<Runnable>any(), anyInt());
//
//        assertThrows(ReverseTransactionException.class, () -> transactionService.reverseTransaction(1));
//        verify(retryUtility).performWithRetry(Mockito.<Runnable>any(), anyInt());
//    }
//}