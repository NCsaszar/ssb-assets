package com.SecureSentinel.CardsLoans.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int stockId;
    private double currentPrice;
    private double initialInvestment;
    private double average;
    private int quantity;
    private String stockTicker;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolioId", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private StockPortfolio stockPortfolio;
}
