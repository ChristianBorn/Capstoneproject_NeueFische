package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Consumption;
import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.stock.StockRepository;
import org.junit.jupiter.api.Test;

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
        //Given
        //When
        when(mockHorseRepository.findAll()).thenReturn(List.of());
        List<Horse> expected = List.of();
        List<Horse> actual = service.getAllHorses();
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void addNewHorse_AndExpectHorse_200() {
        //Given
        Horse newHorse = new Horse(null, "name", "owner", null);
        //When
        when(mockHorseRepository.save(any())).thenReturn(newHorse.withId("1"));
        //Then
        Horse actual = service.addNewHorse(newHorse);
        Horse expected = newHorse.withId("1");
        assertEquals(expected, actual);
    }

    @Test
    void deleteHorse_AndExpectSuccess() {
        //Given
        String idToDelete = "1";
        //When
        when(mockHorseRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(mockHorseRepository).deleteById(idToDelete);
        //Then
        assertTrue(service.deleteHorse(idToDelete));
        verify(mockHorseRepository).existsById(idToDelete);
    }

    @Test
    void deleteHorse_AndExpectException_404() {
        //Given
        String idToDelete = "1";
        //When
        when(mockHorseRepository.existsById(idToDelete))
                .thenReturn(false);
        doNothing().when(mockHorseRepository).deleteById(idToDelete);
        //Then
        try {
            service.deleteHorse(idToDelete);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Kein Eintrag für die gegebene ID gefunden", e.getMessage());
            verify(mockHorseRepository).existsById(idToDelete);
        }
    }

    @Test
    void UpdateHorse_AndExpectSuccess_201() {
        //Given
        Horse newHorse = new Horse("id", "name", "owner",
                List.of(new Consumption("1", "name", new BigDecimal("0"))));
        //When
        when(mockStockRepository.existsById("1")).thenReturn(true);
        when(mockHorseRepository.existsById("id")).thenReturn(false);
        //Then
        assertFalse(service.updateHorse(newHorse));
        verify(mockHorseRepository).save(any());

    }

    @Test
    void UpdateHorse_AndExpectSuccess_200() {
        //Given
        Horse newHorse = new Horse("id", "name", "owner",
                List.of(new Consumption("1", "name", new BigDecimal("0"))));
        //When
        when(mockStockRepository.existsById("1")).thenReturn(true);
        when(mockHorseRepository.existsById("id")).thenReturn(true);
        //Then
        assertTrue(service.updateHorse(newHorse));
        verify(mockHorseRepository).save(newHorse);

    }

    @Test
    void UpdateHorse_AndExpectException_400() {
        //Given
        Horse newHorse = new Horse("id", "name", "owner",
                List.of(new Consumption("1", "name", new BigDecimal("0")),
                        new Consumption("1", "name", new BigDecimal("0"))));
        //When
        //Then
        try {
            service.updateHorse(newHorse);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("IDs of consumptionItems must be unique for every horse", e.getMessage());
        }
    }

    @Test
    void UpdateHorse_NoMatchingStockItem_AndExpectException_400() {
        //Given
        Horse newHorse = new Horse("id", "name", "owner",
                List.of(new Consumption("1", "name", new BigDecimal("0"))));
        //When
        when(mockStockRepository.existsById("1")).thenReturn(false);
        //Then
        try {
            service.updateHorse(newHorse);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Consumption item not in stock", e.getMessage());
        }
    }
}
