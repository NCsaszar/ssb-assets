package com.SecureSentinel.CardsLoans.DTO;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UpdateLoanDTO {
    private double annualPercentageRate;
    private int termMonths;
    private int maxAmount;
    private int minAmount;
    private boolean loanStatus;
}
