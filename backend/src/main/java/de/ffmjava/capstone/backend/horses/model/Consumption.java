package de.ffmjava.capstone.backend.horses.model;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public record Consumption(
        @Id
        String id,
        String name,
        BigDecimal dailyConsumption
) {
}
