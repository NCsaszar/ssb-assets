package com.secure_sentinel.accounts.accounts_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AccountCreationRequestDTO {
    @NotNull(message = "User ID is required")
    @Min(value = 1, message = "User ID must be positive")
    @Schema(example = "189")
    private final Integer userId;

    @NotNull(message = "Account type is required")
    @Pattern(regexp = "^(?i)(CHECKING|SAVINGS|CREDIT)$", message = "Invalid account type")
    @Schema(
            example = "Checking")
    private final String accountType;

    @Min(value = 0, message = "Initial balance must be non-negative")
    @NotNull(message = "Initial balance is required")
    @Schema(example = "100.0")
    private final Double initialBalance;

    private final String programName;


    private AccountCreationRequestDTO(Builder builder) {
        this.userId = builder.userId;
        this.accountType = builder.accountType;
        this.initialBalance = builder.initialBalance;
        this.programName = builder.programName;
    }

    public static Builder builder(Integer userId, String accountType, Double initialBalance) {
        return new Builder(userId, accountType, initialBalance);
    }

    public static class Builder {
        private Integer userId;
        private String accountType;
        private Double initialBalance;
        private String programName;

        private Builder(Integer userId, String accountType, Double initialBalance) {
            this.userId = userId;
            this.accountType = accountType;
            this.initialBalance = initialBalance;
        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder accountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder initialBalance(Double initialBalance) {
            this.initialBalance = initialBalance;
            return this;
        }

        public Builder programName(String programName) {
            this.programName = programName;
            return this;
        }

        public AccountCreationRequestDTO build() {
            return new AccountCreationRequestDTO(this);
        }
    }
}