package de.ffmjava.capstone.backend.horses;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HorseService {

    private final HorseRepository repository;

    public List<Horse> getAllHorses() {
        return repository.findAll();
    }
}
