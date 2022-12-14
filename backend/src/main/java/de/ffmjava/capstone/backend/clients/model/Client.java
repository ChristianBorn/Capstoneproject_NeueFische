package de.ffmjava.capstone.backend.clients.model;

import de.ffmjava.capstone.backend.horses.model.Horse;
import lombok.With;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@With
public record Client(
        String id,
        @NotBlank(message = "Feld \"Name\" darf nicht leer sein")
        String name,
        @Nullable
        List<Horse> ownsHorse,
        LocalDateTime clientSince
) {
}
