package com.SecureSentinel.CardsLoans.Repository;

import com.SecureSentinel.CardsLoans.Model.StockPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPortfolioRepository extends JpaRepository<StockPortfolio,Integer> {
    StockPortfolio findStockPortfolioByUserId(Integer userId);
}
