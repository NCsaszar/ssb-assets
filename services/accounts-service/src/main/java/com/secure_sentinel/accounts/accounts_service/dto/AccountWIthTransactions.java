package com.secure_sentinel.accounts.accounts_service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AccountWIthTransactions {
    private AccountDTO account;
    private List<TransactionDTO> transactions;
}