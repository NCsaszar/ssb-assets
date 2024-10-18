package com.SecureSentinel.CardsLoans.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseStockDTO {
    private String stockTicker;
    private double initialInvestment;
    private int quantity;
}
