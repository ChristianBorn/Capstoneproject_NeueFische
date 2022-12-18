package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.horses.HorseRepository;
import de.ffmjava.capstone.backend.horses.model.AggregatedConsumption;
import de.ffmjava.capstone.backend.horses.model.Consumption;
import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.stock.model.StockItem;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    public static final String AGGREGATED_CONSUMPTION_CACHE = "AGGREGATED_CONSUMPTION_CACHE";
    private final StockRepository stockRepository;
    private final HorseRepository horseRepository;

    public List<StockItem> getAllStockItems() {
        return stockRepository.findAll();
    }

    @CacheEvict(value = AGGREGATED_CONSUMPTION_CACHE, allEntries = true)
    public boolean deleteStockItem(String id) throws IllegalArgumentException {
        if (!stockRepository.existsById(id)) {
            throw new IllegalArgumentException("Kein Eintrag für die gegebene ID gefunden");
        }
        List<Horse> horsesWithStockItemId = horseRepository.findHorsesByConsumptionId(id);

        if (!horsesWithStockItemId.isEmpty()) {
            List<Horse> horsesToUpdate = new ArrayList<>();

            for (Horse horse : horsesWithStockItemId) {
                List<Consumption> consumptionWithoutStockItem = horse.consumptionList()
                        .stream().
                        filter(consumptionItem -> !consumptionItem.id().equals(id))
                        .toList();
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

    @CacheEvict(value = AGGREGATED_CONSUMPTION_CACHE, allEntries = true)
    public boolean updateStockItem(StockItem updatedStockItem) {
        boolean stockItemExists = stockRepository.existsById(updatedStockItem.id());
        if (!stockItemExists) {
            stockRepository.save(updatedStockItem.withId(UUID.randomUUID().toString()));
        } else {
            stockRepository.save(updatedStockItem);
        }
        return stockItemExists;
    }

    public Optional<StockItem> getStockItemById(String id) {
        return stockRepository.findById(id);
    }

    @Cacheable(value = AGGREGATED_CONSUMPTION_CACHE)
    public Map<String, AggregatedConsumption> getAggregatedConsumptions() {
        return horseRepository.aggregateConsumptions()
                .stream()
                .collect(Collectors.toMap(AggregatedConsumption::id, Function.identity()));
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void subtractConsumption() {
        Map<String, AggregatedConsumption> consumptions = getAggregatedConsumptions();
        List<StockItem> stockItemsToUpdate = stockRepository.findByNameIn(consumptions.keySet().stream().toList());
        List<StockItem> updatedStockItems = stockItemsToUpdate
                .stream()
                .map(singleItem -> {
                    if (singleItem.amountInStock().compareTo(BigDecimal.ZERO) <= 0) {
                        return singleItem;
                    }
                    return singleItem.withAmountInStock(singleItem.amountInStock()
                            .subtract(consumptions.get(singleItem.name()).dailyAggregatedConsumption()));
                })
                .toList();
        stockRepository.saveAll(updatedStockItems);
    }
}
