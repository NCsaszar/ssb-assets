package com.SecureSentinel.CardsLoans.Controller;

import com.SecureSentinel.CardsLoans.DTO.PurchaseStockDTO;
import com.SecureSentinel.CardsLoans.Model.Stock;
import com.SecureSentinel.CardsLoans.Model.StockPortfolio;
import com.SecureSentinel.CardsLoans.Service.ApiService;
import com.SecureSentinel.CardsLoans.Service.StockService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockControllerImpl implements StockController {

    private final StockService stockService;
    private final ApiService apiService;

    public StockControllerImpl(StockService stockService, ApiService apiService) {
        this.stockService = stockService;
        this.apiService = apiService;
    }

    @Override
    public JsonNode getTopGainersAndLosers() {
        return apiService.getTopGainersAndLosers();
    }

    @Override
    public JsonNode searchStock(String stockTicker) {
        return apiService.getStockQuote(stockTicker);
    }

    @Override
    public JsonNode infoStock(String stockTicker) {
        return apiService.getCompanyInfo(stockTicker);
    }

    @Override
    public JsonNode monthlyStock(String stockTicker) {
        return apiService.getMonthlyInfo(stockTicker);
    }

    @Override
    public ResponseEntity<Stock> createStock(Integer portfolioId, Integer accountId, PurchaseStockDTO purchaseStockDTO ) {
        return new ResponseEntity<>(stockService.buyStock(portfolioId, accountId, purchaseStockDTO),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Stock> sellStock(Integer stockId, Integer accountId, Integer quantity) {
        return new ResponseEntity<>(stockService.sellStock(stockId,accountId,quantity),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StockPortfolio> viewPortfolio() {
        return new ResponseEntity<>(stockService.viewPortfolio(),HttpStatus.OK);
    }


}
