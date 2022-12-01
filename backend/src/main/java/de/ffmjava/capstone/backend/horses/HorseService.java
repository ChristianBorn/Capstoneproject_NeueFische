package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public boolean deleteHorse(String id) throws ResponseStatusException {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kein Eintrag für die gegebene ID gefunden");
        }
        repository.deleteById(id);
        return true;

    }
}
