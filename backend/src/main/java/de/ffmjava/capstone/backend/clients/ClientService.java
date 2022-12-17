package de.ffmjava.capstone.backend.clients;

import de.ffmjava.capstone.backend.clients.model.Client;
import de.ffmjava.capstone.backend.horses.model.Horse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    public List<Client> getAllClients() {
        return repository.findAll();
    }

    public Client addNewClient(Client newClient) {
        return repository.save(newClient.withId(UUID.randomUUID().toString()));
    }

    public boolean deleteClient(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Kein Eintrag für die gegebene ID gefunden");
        }
        repository.deleteById(id);
        return true;
    }

    public boolean updateClient(Client updatedClient) throws IllegalArgumentException {
        boolean clientExists = repository.existsById(updatedClient.id());
        if (!updatedClient.ownsHorse().isEmpty()) {
            List<String> assignedHorses = updatedClient.ownsHorse()
                    .stream()
                    .map(Horse::id)
                    .distinct().toList();
            if (updatedClient.ownsHorse().size() != assignedHorses.size()) {
                throw new IllegalArgumentException("A horse can only be owned by one person");
            }
            for (Horse horseOfUpdatedClient : updatedClient.ownsHorse()) {
                Client foundClient = repository.findByOwnsHorseContains(horseOfUpdatedClient);
                if (foundClient != null && !foundClient.id().equals(updatedClient.id())) {
                    throw new IllegalArgumentException("One or more horses are already owned");
                }
            }
        }
        repository.save(updatedClient);
        return clientExists;
    }
}
