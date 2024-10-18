package com.SecureSentinel.CardsLoans.Controller;

import com.SecureSentinel.CardsLoans.DTO.PurchaseStockDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewLoanDTO;
import com.SecureSentinel.CardsLoans.Model.Stock;
import com.SecureSentinel.CardsLoans.Model.StockPortfolio;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/stock")
public interface StockController {

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/topGainersLosers")
    public JsonNode getTopGainersAndLosers();

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/info")
    public JsonNode infoStock(@RequestParam String stockTicker);

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/month")
    public JsonNode monthlyStock(@RequestParam String stockTicker);

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/search")
    public JsonNode searchStock(@RequestParam String stockTicker);

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @PostMapping("/buy")
    public ResponseEntity<Stock> createStock(@RequestParam Integer portfolioId, @RequestParam Integer accountId, @RequestBody PurchaseStockDTO purchaseStockDTO);

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @PutMapping("/sell")
    public ResponseEntity<Stock> sellStock(@RequestParam Integer stockId, @RequestParam Integer accountId, @RequestParam Integer quantity);

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/portfolio")
    public ResponseEntity<StockPortfolio> viewPortfolio();
}
