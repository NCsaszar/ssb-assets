package com.SecureSentinel.CardsLoans.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ViewUserLoanDTO {
    private int userLoanID;
    private int loanID;
    private int userID;
    private int accountID;
    private double loanAmount;
    private boolean loanStatus ;
    private Date loanStartDate;
    private Date loanEndDate;
}
