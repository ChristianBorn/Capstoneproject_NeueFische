package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.stock.model.StockItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends MongoRepository<StockItem, String> {
    StockItem getById(String id);

    boolean existsByName(String name);
}
