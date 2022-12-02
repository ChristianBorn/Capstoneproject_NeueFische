package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.stock.model.StockItem;
import de.ffmjava.capstone.backend.user.CustomApiErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stock/")
@RequiredArgsConstructor
class StockController {

    private final StockService service;

    @GetMapping
    public List<StockItem> getAllStockitems() {
        return service.getAllStockItems();
    }

    @GetMapping("{id}")
    public StockItem getStockItemById(@PathVariable String id) {
        return service.getStockItemById(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStockItem(@PathVariable String id) {
        service.deleteStockItem(id);
    }

    @PostMapping
    public ResponseEntity<Object> addNewStockItem(@Valid @RequestBody StockItem newStockItem, Errors errors) {
        ResponseEntity<Object> errorMessage = CustomApiErrorHandler.handlePossibleErrors(errors);
        if (errorMessage != null) return errorMessage;
        StockItem createdItem = service.addNewStockItem(newStockItem);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Object> updateStockItem(@Valid @RequestBody StockItem updatedStockItem, Errors errors) {
        ResponseEntity<Object> errorMessage = CustomApiErrorHandler.handlePossibleErrors(errors);
        if (errorMessage != null) return errorMessage;
        return service.updateStockItem(updatedStockItem);
    }


}
