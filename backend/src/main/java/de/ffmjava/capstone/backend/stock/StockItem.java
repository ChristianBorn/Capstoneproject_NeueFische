package de.ffmjava.capstone.backend.stock;

import lombok.With;

import java.math.BigDecimal;
@With
public record StockItem(
        String id,
        String name,
        StockType type,
        BigDecimal amountInStock,
        BigDecimal pricePerKilo
) {
}
