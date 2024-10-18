package com.SecureSentinel.CardsLoans.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "loan_id")
    private int loanID;
    @Positive(message = "Annual percentage rate must be a positive value")
    @Column(name = "annual_percentage_rate")
    private double annualPercentageRate;

    @PositiveOrZero(message = "Term months must be a non-negative value")
    @Column(name = "term_months")
    private int termMonths;

    @Column(name = "loan_status")
    private boolean loanStatus = true;

    @Min(value = 1, message = "Maximum amount must be greater than 0")
    @Column(name = "max_amount")
    private int maxAmount;

    @Min(value = 1, message = "Minimum amount must be greater than 0")
    @Column(name = "min_amount")
    private int minAmount;


    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanType loanType;

}
