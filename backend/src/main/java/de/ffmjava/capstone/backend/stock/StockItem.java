package de.ffmjava.capstone.backend.stock;

import java.math.BigDecimal;

public record StockItem(
        String id,
        String name,
        StockType type,
        BigDecimal amountInStock,
        BigDecimal pricePerKilo
) {
}
