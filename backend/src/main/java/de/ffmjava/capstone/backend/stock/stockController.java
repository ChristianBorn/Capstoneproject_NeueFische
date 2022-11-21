package de.ffmjava.capstone.backend.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
