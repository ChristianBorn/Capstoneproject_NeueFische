package de.ffmjava.capstone.backend.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    public ResponseEntity<Object> addNewStockItem(@Valid @RequestBody StockItem newStockItem, Errors errors) {
        ResponseEntity<Object> errorMessage = handlePossibleErrors(errors);
        if (errorMessage != null) return errorMessage;
        StockItem createdItem = service.addNewStockItem(newStockItem);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/overview")
    public ResponseEntity<Object> updateStockItem(@Valid @RequestBody StockItem updatedStockItem, Errors errors) {
        ResponseEntity<Object> errorMessage = handlePossibleErrors(errors);
        if (errorMessage != null) return errorMessage;
        return service.updateStockItem(updatedStockItem);
    }

    @Nullable
    private ResponseEntity<Object> handlePossibleErrors(Errors errors) {
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
        return null;
    }
}
