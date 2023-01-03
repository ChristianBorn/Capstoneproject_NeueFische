package de.ffmjava.capstone.backend.horses.model;

import de.ffmjava.capstone.backend.clients.model.Client;

import javax.validation.constraints.NotBlank;
import java.util.List;

public record HorseDTO(
        String id,
        @NotBlank(message = "Feld \"Name\" darf nicht leer sein")
        String name,
        Client owner,
        List<Consumption> consumptionList
) {
}
