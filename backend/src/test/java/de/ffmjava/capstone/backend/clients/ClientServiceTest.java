package de.ffmjava.capstone.backend.clients;

import de.ffmjava.capstone.backend.clients.model.Client;
import de.ffmjava.capstone.backend.clients.model.ClientDTO;
import de.ffmjava.capstone.backend.horses.HorseRepository;
import de.ffmjava.capstone.backend.horses.model.Horse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private final ClientRepository mockClientRepository = mock(ClientRepository.class);
    private final HorseRepository mockHorseRepository = mock(HorseRepository.class);
    private final ClientService service = new ClientService(mockClientRepository, mockHorseRepository);

    @Test
    void getAllClients_returnEmptyList() {
        //Given
        //When
        when(mockClientRepository.findAll()).thenReturn(List.of());
        //Then
        List<ClientDTO> actual = service.getAllClients();
        List<ClientDTO> expected = List.of();
        assertEquals(expected, actual);
    }

    @Test
    void getAllClients_returnListWithOwnedHorse() {
        //Given
        Horse ownedHorse = new Horse("id", "name", "id", List.of());
        Client retrievedClient = new Client("id", "name", List.of(ownedHorse.id()));
        ClientDTO returnedClient = new ClientDTO("id", "name", List.of(ownedHorse));
        //When
        when(mockClientRepository.findAll()).thenReturn(List.of(retrievedClient));
        when(mockHorseRepository.findAllById(retrievedClient.ownsHorse())).thenReturn(List.of(ownedHorse));
        //Then
        List<ClientDTO> actual = service.getAllClients();
        List<ClientDTO> expected = List.of(returnedClient);
        assertEquals(expected, actual);
    }

    @Test
    void deleteClient_AndExpectSuccess() {
        //Given
        String idToDelete = "1";
        //When
        when(mockClientRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(mockClientRepository).deleteById(idToDelete);
        //Then
        assertTrue(service.deleteClient(idToDelete));
        verify(mockClientRepository).existsById(idToDelete);
    }

    @Test
    void deleteClient_AndExpectException_404() {
        //Given
        String idToDelete = "1";
        //When
        when(mockClientRepository.existsById(idToDelete))
                .thenReturn(false);
        doNothing().when(mockClientRepository).deleteById(idToDelete);
        //Then
        try {
            service.deleteClient(idToDelete);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Kein Eintrag für die gegebene ID gefunden", e.getMessage());
            verify(mockClientRepository).existsById(idToDelete);
        }
    }

    @Test
    void updateClient_WithoutPreownedHorse_AndExpectSuccess_200() {
        //Given
        ClientDTO newClient = new ClientDTO("id", "name", List.of());
        //When
        when(mockClientRepository.existsById("id")).thenReturn(true);
        when(mockClientRepository.findByOwnsHorseContains(any())).thenReturn(null);
        //Then
        assertTrue(service.updateClient(newClient));
    }

    @Test
    void updateClient_WithPreownedHorse_AndExpectSuccess_200() {
        //Given
        Horse ownedHorse = new Horse("id", "name", "owner", List.of());
        Horse horseToAdd = new Horse("id2", "name2", "owner2", List.of());
        Client oldClient = new Client("id", "name", List.of(ownedHorse.id()));
        ClientDTO newClient = new ClientDTO("id", "name", List.of(ownedHorse, horseToAdd));
        //When
        when(mockClientRepository.existsById("id")).thenReturn(true);
        when(mockClientRepository.findByOwnsHorseContains(ownedHorse.id())).thenReturn(oldClient);
        when(mockHorseRepository.findById("id")).thenReturn(Optional.of(ownedHorse));
        when(mockHorseRepository.findById("id2")).thenReturn(Optional.of(horseToAdd));
        //Then
        assertTrue(service.updateClient(newClient));
    }

    @Test
    void updateClient_NoOwnership_AndExpectSuccess_200() {
        //Given
        ClientDTO newClient = new ClientDTO("id", "name", List.of());
        //When
        when(mockClientRepository.existsById("id")).thenReturn(true);
        //Then
        assertTrue(service.updateClient(newClient));
    }

    @Test
    void updateClient_AndExpectSuccess_201() {
        //Given
        ClientDTO newClient = new ClientDTO("id", "name", List.of());
        //When
        when(mockClientRepository.existsById("id")).thenReturn(false);
        when(mockClientRepository.findByOwnsHorseContains(any())).thenReturn(null);
        //Then
        assertFalse(service.updateClient(newClient));
    }

    @Test
    void updateClient_AndExpectException_alreadyOwned() {
        //Given
        Horse horseToAdd = new Horse("id", "name", "owner", List.of());
        ClientDTO newClient = new ClientDTO("id", "name", List.of(horseToAdd));
        Client foundClient = new Client("1", "name2", List.of(horseToAdd.id()));
        //When
        when(mockClientRepository.existsById("id")).thenReturn(false);
        when(mockClientRepository.findByOwnsHorseContains(horseToAdd.id())).thenReturn(foundClient);
        //Then
        try {
            service.updateClient(newClient);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("One or more horses are already owned", e.getMessage());
        }
    }

    @Test
    void updateClient_AndExpectException_duplicate() {
        //Given
        Horse horseToAdd = new Horse("id", "name", "owner", List.of());
        ClientDTO newClient = new ClientDTO("id", "name", List.of(horseToAdd, horseToAdd));
        //When
        when(mockClientRepository.existsById("id")).thenReturn(false);
        when(mockClientRepository.findByOwnsHorseContains(horseToAdd.id())).thenReturn(null);
        //Then
        try {
            service.updateClient(newClient);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A horse can only be owned once by the same person", e.getMessage());
        }
    }

    @Test
    void updateClient_AndExpectException_nonExistingHorse() {
        //Given
        Horse horseToAdd = new Horse("id", "name", "owner", List.of());
        ClientDTO newClient = new ClientDTO("id", "name", List.of(horseToAdd));
        //When
        when(mockClientRepository.existsById("id")).thenReturn(true);
        when(mockClientRepository.findByOwnsHorseContains(horseToAdd.id())).thenReturn(null);
        when(mockHorseRepository.findById(horseToAdd.id())).thenReturn(Optional.empty());
        //Then
        try {
            service.updateClient(newClient);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Horse with id does not exist", e.getMessage());
        }
    }

    @Test
    void updateClient_AndUpdateHorses_Expect200() {
        //Given
        Horse preOwnedHorse = new Horse("1", "name1", "1", List.of());
        Horse ownedHorse = new Horse("2", "name2", "1", List.of());
        ClientDTO clientToUpdate = new ClientDTO("1", "name", List.of(ownedHorse));
        Client foundClient = new Client("1", "name", List.of("1"));
        //When
        when(mockClientRepository.existsById(clientToUpdate.id())).thenReturn(true);
        when(mockClientRepository.findByOwnsHorseContains(ownedHorse.id())).thenReturn(foundClient);
        when(mockHorseRepository.findById(ownedHorse.id())).thenReturn(Optional.of(ownedHorse));
        when(mockClientRepository.findById(clientToUpdate.id())).thenReturn(Optional.of(foundClient));
        when(mockHorseRepository.findAllById(List.of(preOwnedHorse.id()))).thenReturn(List.of(preOwnedHorse));
        //Then
        service.updateClient(clientToUpdate);
        verify(mockHorseRepository).saveAll(List.of(preOwnedHorse.withOwner("")));
    }

    @Test
    void updateClient_AndUpdateNoHorses_Expect200() {
        //Given
        Horse preOwnedHorse = new Horse("1", "name1", "1", List.of());
        ClientDTO clientToUpdate = new ClientDTO("1", "name", List.of(preOwnedHorse));
        Client foundClient = new Client("1", "name", List.of("1"));
        //When
        when(mockClientRepository.existsById(clientToUpdate.id())).thenReturn(true);
        when(mockClientRepository.findByOwnsHorseContains(preOwnedHorse.id())).thenReturn(foundClient);
        when(mockHorseRepository.findById(preOwnedHorse.id())).thenReturn(Optional.of(preOwnedHorse));
        when(mockClientRepository.findById(clientToUpdate.id())).thenReturn(Optional.of(foundClient));
        when(mockHorseRepository.findAllById(List.of(preOwnedHorse.id()))).thenReturn(List.of(preOwnedHorse));
        //Then
        service.updateClient(clientToUpdate);
        verify(mockHorseRepository).saveAll(List.of());
    }

    @Test
    void createClientFromClientDTO() {
        //Given
        Horse ownedHorse = new Horse("1", "name", "2", List.of());
        ClientDTO client = new ClientDTO("2", "name", List.of((ownedHorse)));
        //When

        //Then
        Client actual = Client.createClientFromDTO(client);
        Client expected = new Client("2", "name", List.of(ownedHorse.id()));

        assertEquals(expected, actual);
    }

    @Test
    void createClientDTOFromClient() {
        //Given
        Horse ownedHorse = new Horse("1", "name", "2", List.of());
        Client client = new Client("2", "name", List.of((ownedHorse.id())));
        //When
        //Then
        ClientDTO actual = ClientDTO.createDTOFromClient(client, List.of(ownedHorse));
        ClientDTO expected = new ClientDTO("2", "name", List.of(ownedHorse));

        assertEquals(expected, actual);
    }
}