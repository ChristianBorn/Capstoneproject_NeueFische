package de.ffmjava.capstone.backend.clients;

import de.ffmjava.capstone.backend.clients.model.Client;
import de.ffmjava.capstone.backend.horses.model.Horse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
        Client newClient = new Client(null, "name", List.of());

        doReturn(newClient.withId(UUID.randomUUID().toString())).when(mockRepository).save(any());

        Client actual = service.addNewClient(newClient);

        Client expected = newClient.withId(actual.id());

        assertEquals(expected, actual);
    }

    @Test
    void deleteClient_AndExpectSuccess() {
        String idToDelete = "1";
        when(mockRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(mockRepository).deleteById(idToDelete);

        assertTrue(service.deleteClient(idToDelete));
        verify(mockRepository).existsById(idToDelete);
    }

    @Test
    void deleteClient_AndExpectException_404() {
        String idToDelete = "1";
        when(mockRepository.existsById(idToDelete))
                .thenReturn(false);
        doNothing().when(mockRepository).deleteById(idToDelete);
        try {
            service.deleteClient(idToDelete);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Kein Eintrag für die gegebene ID gefunden", e.getMessage());
            verify(mockRepository).existsById(idToDelete);
        }
    }

    @Test
    void updateClient_AndExpectSuccess_200() {
        Client newClient = new Client("id", "name", List.of());
        when(mockRepository.existsById("id")).thenReturn(true);
        when(mockRepository.findByOwnsHorseContains(any())).thenReturn(null);
        assertTrue(service.updateClient(newClient));
    }

    @Test
    void updateClient_AndExpectSuccess_201() {
        Client newClient = new Client("id", "name", List.of());
        when(mockRepository.existsById("id")).thenReturn(false);
        when(mockRepository.findByOwnsHorseContains(any())).thenReturn(null);
        assertFalse(service.updateClient(newClient));
    }

    @Test
    void updateClient_AndExpectException_alreadyOwned() {
        Horse horseToAdd = new Horse("id", "name", "owner", List.of());
        Client newClient = new Client("id", "name", List.of(horseToAdd));
        Client foundClient = new Client("1", "name2", List.of(horseToAdd));
        when(mockRepository.existsById("id")).thenReturn(false);
        when(mockRepository.findByOwnsHorseContains(horseToAdd)).thenReturn(foundClient);

        try {
            service.updateClient(newClient);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("One or more horses are already owned", e.getMessage());
        }
    }

    @Test
    void updateClient_AndExpectException_duplicate() {
        Horse horseToAdd = new Horse("id", "name", "owner", List.of());
        Client newClient = new Client("id", "name", List.of(horseToAdd, horseToAdd));
        when(mockRepository.existsById("id")).thenReturn(false);
        when(mockRepository.findByOwnsHorseContains(horseToAdd)).thenReturn(null);

        try {
            service.updateClient(newClient);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A horse can only be owned by one person", e.getMessage());
        }
    }
}