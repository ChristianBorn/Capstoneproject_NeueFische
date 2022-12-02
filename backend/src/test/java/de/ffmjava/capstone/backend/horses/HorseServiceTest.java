package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HorseServiceTest {

    private final HorseRepository mockRepository = mock(HorseRepository.class);
    private final HorseService service = new HorseService(mockRepository);

    @Test
    void getAllHorses() {
        when(mockRepository.findAll()).thenReturn(List.of());

        List<Horse> expected = List.of();
        List<Horse> actual = service.getAllHorses();

        assertEquals(expected, actual);
    }

    @Test
    void addNewHorse_AndExpectHorse() {
        Horse newHorse = new Horse(null, "name", "owner", null);

        when(mockRepository.save(any())).thenReturn(newHorse.withId("1"));

        Horse actual = service.addNewHorse(newHorse);

        Horse expected = newHorse.withId("1");

        assertEquals(expected, actual);
    }

    @Test
    void deleteHorse_AndExpectSuccess() {
        String idToDelete = "1";
        when(mockRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(mockRepository).deleteById(idToDelete);

        assertTrue(service.deleteHorse(idToDelete));
        verify(mockRepository).existsById(idToDelete);
    }

    @Test
    void deleteHorse_AndExpectException_404() {
        String idToDelete = "1";
        when(mockRepository.existsById(idToDelete))
                .thenReturn(false);
        doNothing().when(mockRepository).deleteById(idToDelete);
        try {
            service.deleteHorse(idToDelete);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals("404 NOT_FOUND \"Kein Eintrag für die gegebene ID gefunden\"", e.getMessage());
            verify(mockRepository).existsById(idToDelete);
        }
    }
}
