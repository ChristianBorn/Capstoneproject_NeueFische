package de.ffmjava.capstone.backend.clients;

import de.ffmjava.capstone.backend.clients.model.Client;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClientServiceTest {

    private final ClientRepository mockRepository = mock(ClientRepository.class);
    private final ClientService service = new ClientService(mockRepository);

    @Test
    void getAllClients() {
        when(mockRepository.findAll()).thenReturn(List.of());

        List<Client> actual = service.getAllClients();
        List<Client> expected = List.of();

        assertEquals(expected, actual);
    }
}