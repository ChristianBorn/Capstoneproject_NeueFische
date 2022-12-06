package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.stock.model.StockItem;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockServiceTest {

    private final StockRepository mockRepository = mock(StockRepository.class);
    private final StockService service = new StockService(mockRepository);

    @Test
    void getAllStockItems_AndExpectEmptyList() {

        when(mockRepository.findAll()).thenReturn(List.of());

        List<StockItem> expected = List.of();
        List<StockItem> actual = service.getAllStockItems();

        assertEquals(expected, actual);
    }

    @Test
    void getItemById_AndExpectStockItem() {
        StockItem returnedItem = new StockItem("1", "name", StockType.FUTTER, new BigDecimal("42.0"), new BigDecimal("42.0"));

        when(mockRepository.getById("1")).thenReturn(returnedItem);

        StockItem expected = returnedItem;
        StockItem actual = service.getStockItemById("1");

        assertEquals(expected, actual);
    }

    @Test
    void getAllStockItems_AndExpectListWithOneElement() {
        StockItem newItem = new StockItem("1", "name", StockType.FUTTER, new BigDecimal("42.0"), new BigDecimal("42.0"));
        StockItem itemToReturn = newItem.withName("new Name")
                .withType(StockType.EINSTREU)
                .withAmountInStock(new BigDecimal("3.4"))
                .withPricePerKilo(new BigDecimal("3.76"));
        when(mockRepository.findAll()).thenReturn(List.of(itemToReturn));

        List<StockItem> expected = List.of(itemToReturn);
        List<StockItem> actual = service.getAllStockItems();

        assertEquals(expected, actual);

    }

    @Test
    void addNewStockItem_AndExpectStockItem_200() {
        StockItem newStockItem = new StockItem(null, "name", StockType.FUTTER, BigDecimal.ONE, BigDecimal.ONE);

        doReturn(newStockItem.withId("1")).when(mockRepository).save(any());

        ResponseEntity<Object> actual = service.addNewStockItem(newStockItem);

        ResponseEntity<Object> expected = new ResponseEntity<>(newStockItem.withId("1"), HttpStatus.CREATED);

        assertEquals(expected, actual);
    }

    @Test
    void addNewStockItem_AndExpect_419() {
        StockItem newStockItem = new StockItem(null, "name", StockType.FUTTER, BigDecimal.ONE, BigDecimal.ONE);

        when(mockRepository.existsByName("name")).thenReturn(true);

        ResponseEntity<Object> actual = service.addNewStockItem(newStockItem);

        ResponseEntity<Object> expected = new ResponseEntity<>("Der angegebene Name ist bereits vergeben", HttpStatus.CONFLICT);

        assertEquals(expected, actual);
    }

    @Test
    void deleteStockItem_AndExpectSuccess() {
        String idToDelete = "1";
        when(mockRepository.existsById(idToDelete)).thenReturn(true);
        doNothing().when(mockRepository).deleteById(idToDelete);

        assertTrue(service.deleteStockItem(idToDelete));
        verify(mockRepository).existsById(idToDelete);
    }

    @Test
    void deleteStockItem_AndExpectException_404() {
        String idToDelete = "1";
        ResponseStatusException expectedException = new ResponseStatusException(HttpStatus.NOT_FOUND, "Kein Eintrag für die gegebene ID gefunden");
        when(mockRepository.existsById(idToDelete))
                .thenReturn(false);
        doNothing().when(mockRepository).deleteById(idToDelete);
        try {
            service.deleteStockItem(idToDelete);
            fail();
        } catch (ResponseStatusException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
            verify(mockRepository).existsById(idToDelete);
        }
    }

}