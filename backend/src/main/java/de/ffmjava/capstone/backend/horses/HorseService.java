package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HorseService {

    private final HorseRepository repository;

    public List<Horse> getAllHorses() {
        return repository.findAll();
    }

    public Horse addNewHorse(Horse newHorse) {
        newHorse = newHorse.withId(UUID.randomUUID().toString());
        return repository.save(newHorse);

    }
}
