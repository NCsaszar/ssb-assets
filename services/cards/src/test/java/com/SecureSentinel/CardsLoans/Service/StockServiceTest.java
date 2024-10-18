package com.SecureSentinel.CardsLoans.Service;

import com.SecureSentinel.CardsLoans.DTO.PurchaseStockDTO;
import com.SecureSentinel.CardsLoans.Model.Stock;
import com.SecureSentinel.CardsLoans.Model.StockPortfolio;
import com.SecureSentinel.CardsLoans.Repository.StockPortfolioRepository;
import com.SecureSentinel.CardsLoans.Repository.StockRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    @Mock
    private  StockRepository stockRepository;

    @Mock
    private StockPortfolioRepository stockPortfolioRepository;

    @Mock
    private  ApiService apiService;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPortfolio() {
        int userId = 123;
        StockPortfolio stockPortfolio = new StockPortfolio();
        stockPortfolio.setUserId(userId);
        when(stockPortfolioRepository.save(any(StockPortfolio.class))).thenReturn(stockPortfolio);

        StockPortfolio createdPortfolio = stockService.createPortfolio(userId);

        assertNotNull(createdPortfolio);
        assertEquals(userId, createdPortfolio.getUserId());
        verify(stockPortfolioRepository, times(1)).save(any(StockPortfolio.class));
    }

    @Test
    void viewPortfolio() {
        int userId = 123;

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(String.valueOf(userId));

        // Prepare test data
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock());
        StockPortfolio existingPortfolio = new StockPortfolio();
        existingPortfolio.setUserId(userId);
        existingPortfolio.setStocks(stocks);

        when(stockPortfolioRepository.findStockPortfolioByUserId(userId)).thenReturn(existingPortfolio);
        when(stockRepository.save(any(Stock.class))).thenReturn(new Stock());

        StockPortfolio viewedPortfolio = stockService.viewPortfolio();

        assertNotNull(viewedPortfolio);
        assertEquals(existingPortfolio, viewedPortfolio);
        verify(stockRepository, times(stocks.size())).save(any(Stock.class));
        verify(stockPortfolioRepository, times(1)).save(existingPortfolio);
    }

    @Test
    void buyStock() {
        Integer portfolioId = 1;
        Integer accountId = 1;
        PurchaseStockDTO purchaseStockDTO = new PurchaseStockDTO("IBM", 100.0, 5);

        when(stockService.getCurrentPrice(purchaseStockDTO.getStockTicker())).thenReturn(150.0);

        StockPortfolio stockPortfolio = new StockPortfolio();
        stockPortfolio.setPortfolioId(portfolioId);
        when(stockPortfolioRepository.getById(portfolioId)).thenReturn(stockPortfolio);

        when(stockRepository.getStockByStockTickerAndStockPortfolio(purchaseStockDTO.getStockTicker(), stockPortfolio))
                .thenReturn(null);

        Stock boughtStock = stockService.buyStock(portfolioId, accountId, purchaseStockDTO);

        assertEquals(purchaseStockDTO.getStockTicker(), boughtStock.getStockTicker());
        assertEquals(purchaseStockDTO.getInitialInvestment(), boughtStock.getInitialInvestment());
        assertEquals(purchaseStockDTO.getQuantity(), boughtStock.getQuantity());
        assertEquals(purchaseStockDTO.getInitialInvestment() / purchaseStockDTO.getQuantity(), boughtStock.getAverage());
        assertEquals(150.0, boughtStock.getCurrentPrice());

        verify(apiService, times(1)).transferMoney(accountId, purchaseStockDTO.getInitialInvestment());
        verify(stockRepository, times(1)).save(boughtStock);
    }

    @Test
    void sellStock() {
        Integer stockId = 1;
        Integer accountId = 1;
        Integer quantity = 5;

        Stock stock = new Stock();
        stock.setStockId(stockId);
        stock.setQuantity(10);
        stock.setCurrentPrice(150.0);

        when(stockRepository.getById(stockId)).thenReturn(stock);

        Stock soldStock = stockService.sellStock(stockId, accountId, quantity);

        assertNotNull(soldStock);
        assertEquals(stockId, soldStock.getStockId());
        assertEquals(5, soldStock.getQuantity());
        verify(stockRepository, times(1)).save(stock);
        verify(apiService, times(1)).depositMoney(eq(accountId), anyDouble());
    }

    @Test
    void getCurrentPrice() {
        String symbol = "IBM";
        String jsonResponse = "{\"Global Quote\":{\"05. price\":\"150.0\"}}";

        Double currentPrice = stockService.getCurrentPrice(symbol);

        assertEquals(null, currentPrice);
    }
}