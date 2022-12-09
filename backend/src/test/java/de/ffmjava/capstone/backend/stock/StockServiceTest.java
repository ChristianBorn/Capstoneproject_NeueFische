package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.horses.HorseRepository;
import de.ffmjava.capstone.backend.horses.model.AggregatedConsumption;
import de.ffmjava.capstone.backend.horses.model.Consumption;
import de.ffmjava.capstone.backend.horses.model.Horse;
import de.ffmjava.capstone.backend.stock.model.StockItem;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockServiceTest {

    private final StockRepository mockStockRepository = mock(StockRepository.class);
    private final HorseRepository mockHorseRepository = mock(HorseRepository.class);

    private final StockService service = new StockService(mockStockRepository, mockHorseRepository);

    @Test
    void getAllStockItems_AndExpectEmptyList() {
        //Given
        //When
        when(mockStockRepository.findAll()).thenReturn(List.of());
        List<StockItem> expected = List.of();
        List<StockItem> actual = service.getAllStockItems();
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void getItemById_AndExpectStockItem() {
        //Given
        Optional<StockItem> returnedItem = Optional.of(new StockItem("1", "name", StockType.FUTTER, new BigDecimal("42.0"), new BigDecimal("42.0")));
        //When
        when(mockStockRepository.findById("1")).thenReturn(returnedItem);
        Optional<StockItem> expected = returnedItem;
        Optional<StockItem> actual = service.getStockItemById("1");
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void getAllStockItems_AndExpectListWithOneElement() {
        //Given
        StockItem newItem = new StockItem("1", "name", StockType.FUTTER, new BigDecimal("42.0"), new BigDecimal("42.0"));
        StockItem itemToReturn = newItem.withName("new Name")
                .withType(StockType.EINSTREU)
                .withAmountInStock(new BigDecimal("3.4"))
                .withPricePerKilo(new BigDecimal("3.76"));
        //When
        when(mockStockRepository.findAll()).thenReturn(List.of(itemToReturn));
        List<StockItem> expected = List.of(itemToReturn);
        List<StockItem> actual = service.getAllStockItems();
        //Then
        assertEquals(expected, actual);

    }

    @Test
    void addNewStockItem_AndExpectStockItem_200() {
        //Given
        StockItem newStockItem = new StockItem(null, "name", StockType.FUTTER, BigDecimal.ONE, BigDecimal.ONE);
        //When
        doReturn(newStockItem.withId("1")).when(mockStockRepository).save(any());
        StockItem actual = service.addNewStockItem(newStockItem);
        StockItem expected = newStockItem.withId("1");
        //Then
        assertEquals(expected, actual);
    }

    @Test
    void addNewStockItem_AndExpect_409() {
        //Given
        StockItem newStockItem = new StockItem(null, "name", StockType.FUTTER, BigDecimal.ONE, BigDecimal.ONE);
        //When
        when(mockStockRepository.existsByName("name")).thenReturn(true);
        //Then
        assertThrows(StockItemAlreadyExistsException.class, () -> service.addNewStockItem(newStockItem));
    }

    @Test
    void deleteStockItem_AndExpectSuccess() {
        //Given
        String idToDelete = "1";
        //When
        when(mockStockRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(mockStockRepository).deleteById(idToDelete);
        //Then
        assertTrue(service.deleteStockItem(idToDelete));
        verify(mockStockRepository).existsById(idToDelete);
    }

    @Test
    void deleteStockItem_AndExpectException_404() {
        //Given
        String idToDelete = "1";
        ResponseStatusException expectedException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Kein Eintrag für die gegebene ID gefunden");
        //When
        when(mockStockRepository.existsById(idToDelete))
                .thenReturn(false);
        doNothing().when(mockStockRepository).deleteById(idToDelete);
        //Then
        try {
            service.deleteStockItem(idToDelete);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
            verify(mockStockRepository).existsById(idToDelete);
        }
    }

    @Test
    void deleteStockItem_noConsumptionToDelete_200() {
        //Given
        String idToDelete = "1";
        //When
        when(mockStockRepository.existsById(idToDelete))
                .thenReturn(true);
        when(mockHorseRepository.findHorsesByConsumptionId("1")).thenReturn(new ArrayList<>());
        //Then
        assertTrue(service.deleteStockItem("1"));
    }

    @Test
    void deleteStockItem_cascadingDeleteConsumption_200() {
        //Given
        String idToDelete = "1";
        Horse horseWithoutConsumption = new Horse("id", "name", "owner", new ArrayList<>());
        Horse horseWithConsumption = horseWithoutConsumption
                .withConsumptionList(new ArrayList<>(List.of(new Consumption("1", "name", new BigDecimal("0")))));
        List<Horse> retrievedHorses = new ArrayList<>(List.of(horseWithConsumption));
        //When
        when(mockStockRepository.existsById(idToDelete))
                .thenReturn(true);
        when(mockHorseRepository.findHorsesByConsumptionId(idToDelete))
                .thenReturn(retrievedHorses);
        when(mockHorseRepository.saveAll(List.of(horseWithoutConsumption)))
                .thenReturn(List.of(horseWithoutConsumption));
        //Then
        assertTrue(service.deleteStockItem("1"));
        verify(mockHorseRepository).findHorsesByConsumptionId(any());
    }

    @Test
    void getAggregatedConsumptions() {
        AggregatedConsumption consumption = new AggregatedConsumption("Hafer", new BigDecimal("1.0"));

        when(mockHorseRepository.aggregateConsumptions()).thenReturn(List.of(
                consumption));
        Map<String, AggregatedConsumption> expected = new HashMap<>(
                Map.of("Hafer", consumption));
        Map<String, AggregatedConsumption> actual = service.getAggregatedConsumptions();

        assertEquals(expected, actual);
    }

}