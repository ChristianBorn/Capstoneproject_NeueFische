package de.ffmjava.capstone.backend.horses;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/horses/overview")
@RequiredArgsConstructor
public class HorseController {

    private final HorseService service;

    @GetMapping
    @RequestMapping
    public List<Horse> getAllHorses() {
        return service.getAllHorses();
    }
}
