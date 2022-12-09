package de.ffmjava.capstone.backend.horses.model;

import lombok.With;

@With
public record AggregatedConsumption(
        String id,
        String dailyAggregatedConsumption
) {
}
