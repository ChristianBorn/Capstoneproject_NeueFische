package de.ffmjava.capstone.backend.clients;

import de.ffmjava.capstone.backend.clients.model.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void addNewClient_AndExpectClient() {
        Client newClient = new Client(null, "name", List.of(), LocalDateTime.now());

        doReturn(newClient.withId("1")).when(mockRepository).save(any());

        Client actual = service.addNewClient(newClient);

        Client expected = newClient.withId("1");

        assertEquals(expected, actual);
    }
}