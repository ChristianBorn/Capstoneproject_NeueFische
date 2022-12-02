package de.ffmjava.capstone.backend.horses.model;

import lombok.With;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.List;

@With
public record Horse(
        @Id
        String id,
        @NotBlank(message = "Feld \"Name\" darf nicht leer sein")
        String name,
        @NotBlank(message = "Feld \"Besitzer\" darf nicht leer sein")
        String owner,
        List<Consumption> consumptionList
) {
}
