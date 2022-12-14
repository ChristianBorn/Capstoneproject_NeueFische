package de.ffmjava.capstone.backend.clients;

import de.ffmjava.capstone.backend.clients.model.Client;
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
}
