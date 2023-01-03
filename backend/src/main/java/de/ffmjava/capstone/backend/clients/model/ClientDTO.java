package de.ffmjava.capstone.backend.clients.model;

import de.ffmjava.capstone.backend.horses.model.Horse;

import javax.validation.constraints.NotBlank;
import java.util.List;

public record ClientDTO(
        String id,
        @NotBlank(message = "Feld \"Name\" darf nicht leer sein")
        String name,
        List<Horse> ownsHorse
) {
        public static ClientDTO createDTOFromClient(Client client, List<Horse> ownedHorses) {
                return new ClientDTO(client.id(),
                        client.name(),
                        ownedHorses);
        }
}
