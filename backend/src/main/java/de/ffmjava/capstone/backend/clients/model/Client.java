package de.ffmjava.capstone.backend.clients.model;

import de.ffmjava.capstone.backend.horses.model.Horse;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@With
public record Client(
        String id,
        String name,
        List<Horse> ownsHorse,
        LocalDateTime clientSince
) {
}
