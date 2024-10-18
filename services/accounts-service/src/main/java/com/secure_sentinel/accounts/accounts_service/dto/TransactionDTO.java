package com.secure_sentinel.accounts.accounts_service.dto;

import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class TransactionDTO {
    private final Integer transactionId;
    private final Integer accountId;
    private final String accountNumber;
    private final TransactionType transactionType;
    private final Double amount;
    private final Double closingBalance;
    private final Boolean isCredit;
    private final String description;
    private final Timestamp dateTime;
}