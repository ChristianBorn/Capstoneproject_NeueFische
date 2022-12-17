package de.ffmjava.capstone.backend.stock;

public class StockItemAlreadyExistsException extends RuntimeException {
    public StockItemAlreadyExistsException(String message) {
        super(message);
    }
}
