package com.SecureSentinel.CardsLoans.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "stock_portfolio")
public class StockPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int portfolioId;
    private int userId;
    private Double totalInvested;
    private int totalValue;

    @OneToMany(mappedBy = "stockPortfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stock> stocks;
}
