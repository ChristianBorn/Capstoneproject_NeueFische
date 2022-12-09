package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.horses.model.AggregatedConsumption;
import de.ffmjava.capstone.backend.stock.model.StockItem;
import de.ffmjava.capstone.backend.user.CustomApiErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock/")
@RequiredArgsConstructor
class StockController {

    private final StockService service;

    @GetMapping
    public List<StockItem> getAllStockitems() {
        return service.getAllStockItems();
    }

    @GetMapping("/consumption/")
    public List<AggregatedConsumption> getAggregatedConsumptions() {
        return service.getAggregatedConsumptions();
    }

    @GetMapping("{id}")
    public Optional<StockItem> getStockItemById(@PathVariable String id) {
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
        try {
            return new ResponseEntity<>(service.addNewStockItem(newStockItem), HttpStatus.CREATED);
        } catch (StockItemAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateStockItem(@Valid @RequestBody StockItem updatedStockItem, Errors errors) {
        ResponseEntity<Object> errorMessage = CustomApiErrorHandler.handlePossibleErrors(errors);
        if (errorMessage != null) return errorMessage;
        return service.updateStockItem(updatedStockItem);
    }


}
