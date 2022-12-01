package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        doReturn(newHorse.withId("1")).when(mockRepository).save(any());

        Horse actual = service.addNewHorse(newHorse);

        Horse expected = newHorse.withId("1");

        assertEquals(expected, actual);
    }
}