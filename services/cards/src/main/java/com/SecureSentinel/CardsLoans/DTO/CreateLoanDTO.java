package com.SecureSentinel.CardsLoans.DTO;

import lombok.Data;


@Data
public class CreateLoanDTO {
    private double annualPercentageRate;
    private int termMonths;
    private int maxAmount;
    private int minAmount;
    private int loanTypeId;

}
