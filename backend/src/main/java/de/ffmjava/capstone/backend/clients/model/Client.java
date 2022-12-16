package de.ffmjava.capstone.backend.clients.model;

import de.ffmjava.capstone.backend.horses.model.Horse;
import lombok.With;

import javax.validation.constraints.NotBlank;
import java.util.List;

@With
public record Client(
        String id,
        @NotBlank(message = "Feld \"Name\" darf nicht leer sein")
        String name,
        List<Horse> ownsHorse
) {
}
