package com.secure_sentinel.accounts.accounts_service.service;

import com.secure_sentinel.accounts.accounts_service.dto.DepositFundsDTO;
import com.secure_sentinel.accounts.accounts_service.dto.PaymentDTO;
import com.secure_sentinel.accounts.accounts_service.dto.TransactionDTO;
import com.secure_sentinel.accounts.accounts_service.dto.TransferFundsDTO;
import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    Page<TransactionDTO> getTransactionsForAccount(Integer accountId, int page, int size, String sortBy,
                                                   LocalDateTime startDate, LocalDateTime endDate,
                                                   Double minAmount, Double maxAmount,
                                                   TransactionType transactionType);

    Page<TransactionDTO> getAllTransactions(int page, int size, String sortBy, LocalDateTime startDate,
                                            LocalDateTime endDate, Double minAmount, Double maxAmount,
                                            TransactionType transactionType, String lastFour);

    List<TransactionDTO> getAllTransactionsForAccount(Integer accountId);

    void depositFunds(DepositFundsDTO requestDTO);

    void makePayment(PaymentDTO requestDTO);

    void transferFunds(TransferFundsDTO requestDTO);

    void reverseTransaction(Integer transactionId);

    byte[] exportTransactions(Integer accountId, LocalDateTime startDate, LocalDateTime endDate, String format) throws IOException;
}