package de.ffmjava.capstone.backend.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository repository;

    public List<StockItem> getAllStockItems() {
        return repository.findAll();
    }

    public StockItem addNewStockItem(StockItem newStockItem) {
        StockItem newStockItemWithId = newStockItem.withId(UUID.randomUUID().toString());
        return repository.save(newStockItemWithId);
    }
}
