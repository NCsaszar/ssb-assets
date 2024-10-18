package com.SecureSentinel.CardsLoans.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserLoanDTO {
    @NotBlank(message = "LoanID is mandatory")
    private int loanID;
    @NotBlank(message = "UserID is mandatory")
    private int userID;
    @NotBlank(message = "AccountID is mandatory")
    private int accountID;
    @NotBlank(message = "Loan Amount is mandatory")
    private double loanAmount;
}
