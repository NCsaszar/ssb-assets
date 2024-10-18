package com.SecureSentinel.CardsLoans.Service;

import com.SecureSentinel.CardsLoans.DTO.PurchaseStockDTO;
import com.SecureSentinel.CardsLoans.Model.Stock;
import com.SecureSentinel.CardsLoans.Model.StockPortfolio;
import com.SecureSentinel.CardsLoans.Repository.StockPortfolioRepository;
import com.SecureSentinel.CardsLoans.Repository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockPortfolioRepository stockPortfolioRepository;
    private final ApiService apiService;

    @Autowired
    public StockService(StockRepository stockRepository, StockPortfolioRepository stockPortfolioRepository, ApiService apiService) {
        this.stockRepository = stockRepository;
        this.stockPortfolioRepository = stockPortfolioRepository;
        this.apiService = apiService;
    }

    public StockPortfolio createPortfolio(int userId){
        StockPortfolio stockPortfolio = new StockPortfolio();
        stockPortfolio.setUserId(userId);

        try {
            return stockPortfolioRepository.save(stockPortfolio);
        } catch (DataIntegrityViolationException e) {
            System.out.println("StockPortfolio for userId " + userId + " already exists.");
            return null;
        }
    }


    public StockPortfolio viewPortfolio() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());

        StockPortfolio stockPortfolio = stockPortfolioRepository.findStockPortfolioByUserId(userId);

        if (stockPortfolio != null) {
            List<Stock> stocks = stockPortfolio.getStocks();

            stocks.forEach(stock -> {
                Double currentPrice = getCurrentPrice(stock.getStockTicker());

                if (currentPrice != null) {
                    stock.setCurrentPrice(currentPrice);
                    stockRepository.save(stock);
                }
            });

            double totalInvested = stocks.stream()
                    .mapToDouble(Stock::getInitialInvestment)
                    .sum();

            double totalValue = stocks.stream()
                    .mapToDouble(stock -> stock.getCurrentPrice() * stock.getQuantity())
                    .sum();

            stockPortfolio.setTotalInvested(totalInvested);
            stockPortfolio.setTotalValue((int)totalValue);

            stockPortfolioRepository.save(stockPortfolio);
        }else{
            createPortfolio(userId);
        }

        return stockPortfolio;
    }

    public Stock buyStock(Integer portfolioId, Integer accountId, PurchaseStockDTO purchaseStockDTO ){

        apiService.transferMoney(accountId, purchaseStockDTO.getInitialInvestment());

        StockPortfolio stockPortfolio = stockPortfolioRepository.getById(portfolioId);

        Stock existingStock = stockRepository.getStockByStockTickerAndStockPortfolio(purchaseStockDTO.getStockTicker(), stockPortfolio);

        if (existingStock != null) {
            existingStock.setInitialInvestment(purchaseStockDTO.getInitialInvestment() + existingStock.getInitialInvestment());
            existingStock.setQuantity(existingStock.getQuantity() + purchaseStockDTO.getQuantity());
            existingStock.setAverage(existingStock.getInitialInvestment()/existingStock.getQuantity());
            existingStock.setCurrentPrice(getCurrentPrice(existingStock.getStockTicker()));
            return stockRepository.save(existingStock);
        } else {
            Stock newStock = new Stock();
            newStock.setStockPortfolio(stockPortfolio);
            newStock.setStockTicker(purchaseStockDTO.getStockTicker());
            newStock.setInitialInvestment(purchaseStockDTO.getInitialInvestment());
            newStock.setQuantity(purchaseStockDTO.getQuantity());
            newStock.setAverage(newStock.getInitialInvestment()/newStock.getQuantity());
            newStock.setCurrentPrice(getCurrentPrice(newStock.getStockTicker()));
            return stockRepository.save(newStock);
        }
    }

    public Stock sellStock(Integer stockId, Integer accountId, Integer quantity){
        Stock stock = stockRepository.getById(stockId);
        double profit = 0;

        if(stock.getQuantity() < quantity){
            return null;
        }

        profit = quantity * stock.getCurrentPrice();

        stock.setQuantity(stock.getQuantity() - quantity);

        stockRepository.save(stock);

        apiService.depositMoney(accountId,profit);

        return stock;
    }


    public Double getCurrentPrice(String symbol) {
        JsonNode stockQuoteResponse = apiService.getStockQuote(symbol);

        if (stockQuoteResponse != null && stockQuoteResponse.has("Global Quote")) {
            JsonNode globalQuote = stockQuoteResponse.get("Global Quote");

            if (globalQuote.has("05. price")) {
                return globalQuote.get("05. price").asDouble();
            }
        }

        return null;
    }


}
