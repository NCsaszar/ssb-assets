package com.secure_sentinel.accounts.accounts_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "account_programs")
public class AccountProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer programId;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "program_name", nullable = false)
    private String programName;

    @Column(name = "monthly_fee")
    private Double monthlyFee = 0.0;

    @Column(name = "apy")
    private Double apy = 0.0;

    @Column(name = "cash_back")
    private Double cashBack = 0.0;

    @Column(name = "rewards_type")
    private String rewardsType;
}