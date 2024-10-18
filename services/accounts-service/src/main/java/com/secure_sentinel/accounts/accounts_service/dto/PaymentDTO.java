package com.secure_sentinel.accounts.accounts_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentDTO {
    @NotNull(message = "Source account ID cannot be null")
    private final Integer sourceAccountId;
    @NotNull(message = "Destination account ID cannot be null")
    private final Integer destinationAccountId;
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    @Min(value = 1, message = "Minimum payment amount is 1")
    private final Double amount;
}