package de.ffmjava.capstone.backend.horses;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Map;

@With
public record Horse(
        @Id
        String id,
        String name,
        String owner,
        Map<String, BigDecimal> consumption
) {
}
