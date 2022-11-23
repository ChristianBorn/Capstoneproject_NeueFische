package de.ffmjava.capstone.backend.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository repository;

    public List<StockItem> getAllStockItems() {
        return repository.findAll();
    }

    public void deleteStockItem(String id) throws ResponseStatusException{
        if(!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kein Eintrag für die gegebene ID gefunden");
        }
        try {
            repository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keine ID im Request vorhanden");
        }
    }
    public StockItem addNewStockItem(StockItem newStockItem) {
        StockItem newStockItemWithId = newStockItem.withId(UUID.randomUUID().toString());
        return repository.save(newStockItemWithId);
    }
}
