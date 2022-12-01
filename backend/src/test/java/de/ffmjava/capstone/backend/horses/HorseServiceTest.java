package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import org.junit.jupiter.api.Test;

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
}