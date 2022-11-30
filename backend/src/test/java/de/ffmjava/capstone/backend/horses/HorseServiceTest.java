package de.ffmjava.capstone.backend.horses;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void testHorseRecord() {
        Horse newHorse = new Horse("id", "name", "owner", List.of(new Consumption("1", "Hafer", new BigDecimal("4.0"))));
        Horse changedHorse = newHorse.withId("1").withName("Peter").withOwner("Hansi").withDailyConsumption(List.of(new Consumption("1", "Hafer", new BigDecimal("4.0"))));
        Horse expected = changedHorse;
        Horse actual = changedHorse;

        assertEquals(expected, actual);
    }
}