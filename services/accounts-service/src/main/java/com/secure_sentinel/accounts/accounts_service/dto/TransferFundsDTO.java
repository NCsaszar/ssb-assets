package com.secure_sentinel.accounts.accounts_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransferFundsDTO {
    @NotNull
    private final Integer sourceAccountId;
    @NotNull
    private final Integer targetAccountId;
    @NotNull
    @Min(value = 1, message = "Amount must be greater than 0")
    private final Double amount;
}