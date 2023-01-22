package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.clients.ClientRepository;
import de.ffmjava.capstone.backend.clients.model.Client;
import de.ffmjava.capstone.backend.horses.model.Consumption;
import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.horses.model.HorseDTO;
import de.ffmjava.capstone.backend.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static de.ffmjava.capstone.backend.stock.StockService.AGGREGATED_CONSUMPTION_CACHE;

@Service
@RequiredArgsConstructor
public class HorseService {

    private final HorseRepository horseRepository;
    private final StockRepository stockRepository;
    private final ClientRepository clientRepository;


    public List<HorseDTO> getAllHorses() {
        List<Horse> retrievedHorses = horseRepository.findAll();
        List<HorseDTO> horsesToReturn = new ArrayList<>();
        for (Horse horse : retrievedHorses) {

            Optional<Client> foundClient = clientRepository.findById(horse.owner());
            if (foundClient.isPresent()) {
                horsesToReturn.add(new HorseDTO(horse.id(), horse.name(), foundClient.get(), horse.consumptionList()));
            }
            else {
                horsesToReturn.add(new HorseDTO(horse.id(), horse.name(), null, horse.consumptionList()));
            }
        }
        return horsesToReturn;
    }

    @CacheEvict(value = AGGREGATED_CONSUMPTION_CACHE, allEntries = true)
    public boolean updateHorse(HorseDTO updatedHorse) throws IllegalArgumentException {
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
        if (!horseExists) {
            horseRepository.save(Horse.createHorseFromDTO(updatedHorse).withId(UUID.randomUUID().toString()));
        } else {
            horseRepository.save(Horse.createHorseFromDTO(updatedHorse));
        }
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
