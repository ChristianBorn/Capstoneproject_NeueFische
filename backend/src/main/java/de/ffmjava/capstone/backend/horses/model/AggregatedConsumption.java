package de.ffmjava.capstone.backend.horses.model;

import lombok.With;

import java.math.BigDecimal;

@With
public record AggregatedConsumption(
        String id,
        BigDecimal dailyAggregatedConsumption
) {
}
