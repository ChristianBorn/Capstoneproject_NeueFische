package de.ffmjava.capstone.backend.stock;

public record StockItem(
        String id,
        String name,
        StockType type,
        double amountInStock,
        double pricePerKilo
) {
}
