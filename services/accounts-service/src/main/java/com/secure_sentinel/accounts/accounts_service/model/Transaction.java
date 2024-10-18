package com.secure_sentinel.accounts.accounts_service.model;

import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_account_id", columnList = "account_id"),
        @Index(name = "idx_date_time", columnList = "date_time")
})
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @Column(name = "date_time", nullable = false)
    private Timestamp dateTime;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "closing_balance", nullable = false)
    private Double closingBalance;
    @Column(name = "is_credit", nullable = false)
    private Boolean isCredit;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @PrePersist
    protected void onCreate() {
        dateTime = new Timestamp(System.currentTimeMillis());
    }
}