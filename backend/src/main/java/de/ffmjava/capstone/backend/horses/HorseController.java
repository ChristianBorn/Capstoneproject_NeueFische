package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.user.CustomApiErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/horses/")
@RequiredArgsConstructor
public class HorseController {

    private final HorseService service;

    @GetMapping
    public List<Horse> getAllHorses() {
        return service.getAllHorses();
    }

    @PostMapping
    public ResponseEntity<Object> addNewHorse(@Valid @RequestBody Horse newHorse, Errors errors) {
        ResponseEntity<Object> errorMessage = CustomApiErrorHandler.handlePossibleErrors(errors);
        if (errorMessage != null) return errorMessage;
        Horse createdHorse = service.addNewHorse(newHorse);
        return new ResponseEntity<>(createdHorse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHorse(@PathVariable String id) throws ResponseStatusException {
        service.deleteHorse(id);
    }
}
