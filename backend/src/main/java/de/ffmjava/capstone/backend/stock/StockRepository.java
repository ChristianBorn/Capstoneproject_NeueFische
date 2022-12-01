package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.stock.model.StockItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<StockItem, String> {
    StockItem getById(String id);
}
