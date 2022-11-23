package de.ffmjava.capstone.backend.stock;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void addNewStockItem_AndExpectStockItem() {
        StockItem newStockItem = new StockItem(null, "name", StockType.FUTTER, BigDecimal.ONE, BigDecimal.ONE);

        doReturn(newStockItem.withId("1")).when(mockRepository).save(any());

        StockItem actual = service.addNewStockItem(newStockItem);

        StockItem expected = newStockItem.withId("1");

        assertEquals(expected, actual);
    }

    @Test
    void deleteStockItem_AndExpectSuccess() {
        String idToDelete = "1";
        when(mockRepository.existsById(idToDelete)).thenReturn(true);
        try {
            doNothing().when(mockRepository).deleteById(idToDelete);
        } catch (Exception e) {
            fail();
        }

    }
}