package de.ffmjava.capstone.backend.stock;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}