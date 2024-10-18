package com.SecureSentinel.CardsLoans.Repository;

import com.SecureSentinel.CardsLoans.Model.Stock;
import com.SecureSentinel.CardsLoans.Model.StockPortfolio;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    public Stock getStockByStockTickerAndStockPortfolio(String stockTicker, StockPortfolio stockPortfolio);
}
