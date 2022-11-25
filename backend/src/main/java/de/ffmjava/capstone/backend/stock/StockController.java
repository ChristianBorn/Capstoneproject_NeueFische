package de.ffmjava.capstone.backend.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
class StockController {

    private final StockService service;

    @GetMapping("/overview")
    public List<StockItem> getAllStockitems() {
        return service.getAllStockItems();
    }

    @DeleteMapping("/overview/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStockItem(@PathVariable String id) throws ResponseStatusException {
        service.deleteStockItem(id);
    }

    @PostMapping("/overview")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<String> addNewStockItem(@Valid @RequestBody StockItem newStockItem, Errors errors) {
        if (errors.hasErrors()) {
            FieldError fieldError;
            String errorMessage = null;
            if (errors.getFieldError() != null) {
                fieldError = errors.getFieldError();
                if (fieldError != null) {
                    errorMessage = fieldError.getDefaultMessage();
                }
            }
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        service.addNewStockItem(newStockItem);
        return new ResponseEntity<>("Neue Position \"<name>\" erfolgreich gespeichert!"
                .replace("<name>", newStockItem.name()), HttpStatus.CREATED);
    }
}
