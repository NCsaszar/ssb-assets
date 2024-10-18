package com.SecureSentinel.CardsLoans.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_loan")
public class UserLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_loan_generator")
    @SequenceGenerator(name = "user_loan_generator", sequenceName = "user_loan_seq", allocationSize = 1)
    @Column(name = "user_loan_id", nullable = false)
    private int userLoanID;
    @Column(name = "user_id")
    private int userID;
    @Column(name = "account_id")
    private int accountID;
    @Positive(message = "Loan amount must be positive")
    @Column(name = "loan_amount")
    private double loanAmount;
    @Column(name = "loan_status")
    private boolean loanStatus = false;
    @Column(name = "loan_start_date")
    private Date loanStartDate;
    @Column(name = "loan_end_date")
    private Date loanEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Loan loan;


}
