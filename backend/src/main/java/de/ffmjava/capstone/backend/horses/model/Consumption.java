package de.ffmjava.capstone.backend.horses.model;

import lombok.With;

import java.math.BigDecimal;

@With
public record Consumption(
        String id,
        String name,
        BigDecimal dailyConsumption
) {
}
