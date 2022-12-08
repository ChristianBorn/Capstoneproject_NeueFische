package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.horses.HorseRepository;
import de.ffmjava.capstone.backend.horses.model.Consumption;
import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.stock.model.StockItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final HorseRepository horseRepository;

    public List<StockItem> getAllStockItems() {
        return stockRepository.findAll();
    }

    public boolean deleteStockItem(String id) throws ResponseStatusException {
        if (!stockRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kein Eintrag für die gegebene ID gefunden");
        }
        List<Horse> horsesWithStockItemId = horseRepository.findHorsesByConsumptionId(id);

        if (!horsesWithStockItemId.isEmpty()) {
            List<Horse> horsesToUpdate = new ArrayList<>();

            for (Horse horse : horsesWithStockItemId) {
                List<Consumption> consumptionWithoutStockItem = horse.consumptionList()
                        .stream().filter(consumptionItem -> !consumptionItem.id().equals(id)).toList();
                horsesToUpdate.add(horse.withConsumptionList(consumptionWithoutStockItem));
            }
            horseRepository.saveAll(horsesToUpdate);
        }
        stockRepository.deleteById(id);
        return true;

    }

    public StockItem addNewStockItem(StockItem newStockItem) {
        if (stockRepository.existsByName(newStockItem.name())) {
            throw new StockItemAlreadyExistsException("Der angegebene Name ist bereits vergeben");
        }
        StockItem newStockItemWithId = newStockItem.withId(UUID.randomUUID().toString());
        return stockRepository.save(newStockItemWithId);
    }

    public ResponseEntity<Object> updateStockItem(StockItem updatedStockItem) {
        boolean stockItemExists = stockRepository.existsById(updatedStockItem.id());
        stockRepository.save(updatedStockItem);
        if (stockItemExists) {
            return new ResponseEntity<>(updatedStockItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updatedStockItem, HttpStatus.CREATED);
        }
    }

    public StockItem getStockItemById(String id) {
        return stockRepository.getById(id);
    }
}
