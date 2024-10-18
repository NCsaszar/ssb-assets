package com.secure_sentinel.accounts.accounts_service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DetailedAccountDTO {
    private final String accountNumber;
    private final String accountType;
    private final Double balance;
    private final Double creditLimit;
    private final Double amountOwed;
    private final String programName;
    private final List<TransactionDTO> transactions;
}