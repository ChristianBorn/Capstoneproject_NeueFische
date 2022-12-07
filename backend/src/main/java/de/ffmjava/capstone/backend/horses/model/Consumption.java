package de.ffmjava.capstone.backend.horses.model;

import lombok.With;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@With
public record Consumption(
        String id,
        String name,
        @Field(targetType = DECIMAL128)
        BigDecimal dailyConsumption
) {
}
