package com.secure_sentinel.accounts.accounts_service.model;


import com.secure_sentinel.accounts.accounts_service.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(name = "balance", nullable = false)
    private Double balance;
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    @Column(name = "credit_limit")
    private Double creditLimit = 0.0;
    @Column(name = "amount_owed")
    private Double amountOwed = 0.0;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private AccountProgram accountProgram;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }
}