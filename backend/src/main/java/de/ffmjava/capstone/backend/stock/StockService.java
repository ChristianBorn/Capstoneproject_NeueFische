package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.stock.model.StockItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public boolean deleteStockItem(String id) throws ResponseStatusException {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kein Eintrag für die gegebene ID gefunden");
        }
        repository.deleteById(id);
        return true;

    }

    public StockItem addNewStockItem(StockItem newStockItem) {
        if (repository.existsByName(newStockItem.name())) {
            throw new StockItemAlreadyExistsException("Der angegebene Name ist bereits vergeben");
        }
        StockItem newStockItemWithId = newStockItem.withId(UUID.randomUUID().toString());
        return repository.save(newStockItemWithId);
    }

    public ResponseEntity<Object> updateStockItem(StockItem updatedStockItem) {
        boolean stockItemExists = repository.existsById(updatedStockItem.id());
        repository.save(updatedStockItem);
        if (stockItemExists) {
            return new ResponseEntity<>(updatedStockItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updatedStockItem, HttpStatus.CREATED);
        }
    }

    public StockItem getStockItemById(String id) {
        return repository.getById(id);
    }
}
