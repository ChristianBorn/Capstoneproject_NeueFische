package de.ffmjava.capstone.backend.horses;

import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.List;

@With
public record Horse(
        @Id
        String id,
        String name,
        String owner,
        List<Consumption> consumption
) {
}
