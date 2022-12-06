package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Consumption;
import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.stock.StockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
    void addNewHorse_AndExpectHorse_200() {
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

    @Test
    void UpdateHorse_AndExpectSuccess_201() {
        Horse newHorse = new Horse("id", "name", "owner",
                List.of(new Consumption("1", "name", new BigDecimal("0"))));
        when(mockStockRepository.existsById("1")).thenReturn(true);
        when(mockHorseRepository.existsById("id")).thenReturn(false);
        assertFalse(service.updateHorse(newHorse));
        verify(mockHorseRepository).save(newHorse);

    }

    @Test
    void UpdateHorse_AndExpectSuccess_200() {
        Horse newHorse = new Horse("id", "name", "owner",
                List.of(new Consumption("1", "name", new BigDecimal("0"))));
        when(mockStockRepository.existsById("1")).thenReturn(true);
        when(mockHorseRepository.existsById("id")).thenReturn(true);
        assertTrue(service.updateHorse(newHorse));
        verify(mockHorseRepository).save(newHorse);

    }

    @Test
    void UpdateHorse_AndExpectException_400() {
        Horse newHorse = new Horse("id", "name", "owner",
                List.of(new Consumption("1", "name", new BigDecimal("0")),
                        new Consumption("1", "name", new BigDecimal("0"))));
        try {
            service.updateHorse(newHorse);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("IDs of consumptionItems must be unique for every horse", e.getMessage());
        }
    }

    @Test
    void UpdateHorse_NoMatchingStockItem_AndExpectException_400() {
        Horse newHorse = new Horse("id", "name", "owner",
                List.of(new Consumption("1", "name", new BigDecimal("0"))));
        when(mockStockRepository.existsById("1")).thenReturn(false);

        try {
            service.updateHorse(newHorse);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Consumption item not in stock", e.getMessage());
        }
    }
}
