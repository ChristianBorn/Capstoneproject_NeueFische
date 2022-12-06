package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.stock.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HorseServiceTest {

    private final HorseRepository mockHorseRepository = mock(HorseRepository.class);
    private final StockRepository mockStockRepository = mock(StockRepository.class);
    private final HorseService service = new HorseService(mockHorseRepository, mockStockRepository);

    @Test
    void getAllHorses() {
        when(mockHorseRepository.findAll()).thenReturn(List.of());

        List<Horse> expected = List.of();
        List<Horse> actual = service.getAllHorses();

        assertEquals(expected, actual);
    }

    @Test
    void addNewHorse_AndExpectHorse() {
        Horse newHorse = new Horse(null, "name", "owner", null);

        when(mockHorseRepository.save(any())).thenReturn(newHorse.withId("1"));

        Horse actual = service.addNewHorse(newHorse);

        Horse expected = newHorse.withId("1");

        assertEquals(expected, actual);
    }

    @Test
    void deleteHorse_AndExpectSuccess() {
        String idToDelete = "1";
        when(mockHorseRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(mockHorseRepository).deleteById(idToDelete);

        assertTrue(service.deleteHorse(idToDelete));
        verify(mockHorseRepository).existsById(idToDelete);
    }

    @Test
    void deleteHorse_AndExpectException_404() {
        String idToDelete = "1";
        when(mockHorseRepository.existsById(idToDelete))
                .thenReturn(false);
        doNothing().when(mockHorseRepository).deleteById(idToDelete);
        try {
            service.deleteHorse(idToDelete);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals("404 NOT_FOUND \"Kein Eintrag für die gegebene ID gefunden\"", e.getMessage());
            verify(mockHorseRepository).existsById(idToDelete);
        }
    }
}
