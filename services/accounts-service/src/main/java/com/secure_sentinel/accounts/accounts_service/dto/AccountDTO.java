package com.secure_sentinel.accounts.accounts_service.dto;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class AccountDTO {
    private final Integer userId;
    private final Integer accountId;
    private final String accountNumber;
    private final String accountType;
    private final String programName;
    private final Double balance;
    private final Double creditLimit;
    private final Double amountOwed;
    private final Boolean isActive;
    private final Timestamp createdAt;

    private AccountDTO(Builder builder) {
        this.userId = builder.userId;
        this.accountId = builder.accountId;
        this.accountNumber = builder.accountNumber;
        this.accountType = builder.accountType;
        this.programName = builder.programName;
        this.balance = builder.balance;
        this.creditLimit = builder.creditLimit;
        this.amountOwed = builder.amountOwed;
        this.isActive = builder.isActive;
        this.createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer userId;
        private Integer accountId;
        private String accountNumber;
        private String accountType;
        private String programName;
        private Double balance;
        private Double creditLimit;
        private Double amountOwed;
        private Boolean isActive;
        private Timestamp createdAt;

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder accountId(Integer accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder accountType(String accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder programName(String programName) {
            this.programName = programName;
            return this;
        }

        public Builder balance(Double balance) {
            this.balance = balance;
            return this;
        }

        public Builder creditLimit(Double creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }

        public Builder amountOwed(Double amountOwed) {
            this.amountOwed = amountOwed;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AccountDTO build() {
            return new AccountDTO(this);
        }
    }
}