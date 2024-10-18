package com.secure_sentinel.accounts.accounts_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepositFundsDTO {
    @NotNull(message = "Account ID cannot be null")
    private Integer accountId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    @Min(value = 1, message = "Minimum deposit amount is 1")
    private Double amount;
}