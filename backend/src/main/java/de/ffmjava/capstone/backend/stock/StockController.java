package de.ffmjava.capstone.backend.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lager")
@RequiredArgsConstructor
class StockController {

    private final StockService service;

    @GetMapping("/ueberblick")
    public List<StockItem> getAllStockitems() {
        return service.getAllStockItems();
    }

    @PostMapping("/ueberblick")
    @ResponseStatus(code = HttpStatus.CREATED)
    public StockItem addNewStockItem(@RequestBody StockItem newStockItem) {
        return service.addNewStockItem(newStockItem);
    }
}
