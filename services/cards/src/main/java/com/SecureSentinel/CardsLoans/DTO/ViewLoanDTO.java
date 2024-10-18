package com.SecureSentinel.CardsLoans.DTO;

import com.SecureSentinel.CardsLoans.Model.LoanType;
import lombok.Data;

@Data
public class ViewLoanDTO {
    private int loanID;
    private String loanType;
    private double annualPercentageRate;
    private int termMonths;
    private int maxAmount;
    private int minAmount;
    private boolean loanStatus;
}
