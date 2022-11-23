package de.ffmjava.capstone.backend.stock;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StockType {
    FUTTER("Futter"),
    EINSTREU("Einstreu"),
    ;
    private final String displayName;

    StockType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
