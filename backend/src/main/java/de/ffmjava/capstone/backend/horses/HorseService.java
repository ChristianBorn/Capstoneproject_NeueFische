package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Consumption;
import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static de.ffmjava.capstone.backend.stock.StockService.AGGREGATED_CONSUMPTION_CACHE;

@Service
@RequiredArgsConstructor
public class HorseService {

    private final HorseRepository horseRepository;
    private final StockRepository stockRepository;

    public List<Horse> getAllHorses() {
        return horseRepository.findAll();
    }

    @CacheEvict(value = AGGREGATED_CONSUMPTION_CACHE, allEntries = true)
    public boolean updateHorse(Horse updatedHorse) throws IllegalArgumentException {
        boolean horseExists = horseRepository.existsById(updatedHorse.id());
        List<String> assignedStockItemIds = updatedHorse.consumptionList()
                .stream()
                .map(Consumption::id)
                .distinct().toList();
        if (updatedHorse.consumptionList().size() != assignedStockItemIds.size()) {
            throw new IllegalArgumentException("IDs of consumptionItems must be unique for every horse");
        }
        for (Consumption consumption : updatedHorse.consumptionList()) {
            if (!stockRepository.existsById(consumption.id())) {
                throw new IllegalArgumentException("Consumption item not in stock");
            }
        }
        horseRepository.save(updatedHorse);
        return horseExists;
    }

    public Horse addNewHorse(Horse newHorse) {
        newHorse = newHorse.withId(UUID.randomUUID().toString());
        return horseRepository.save(newHorse);
    }

    @CacheEvict(value = AGGREGATED_CONSUMPTION_CACHE, allEntries = true)
    public boolean deleteHorse(String id) throws IllegalArgumentException {
        if (!horseRepository.existsById(id)) {
            throw new IllegalArgumentException("Kein Eintrag für die gegebene ID gefunden");
        }
        horseRepository.deleteById(id);
        return true;
    }
}
