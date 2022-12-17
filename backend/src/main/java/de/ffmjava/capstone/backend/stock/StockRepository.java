package de.ffmjava.capstone.backend.stock;

import de.ffmjava.capstone.backend.stock.model.StockItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<StockItem, String> {

    boolean existsByName(String name);
    List<StockItem> findByNameIn(List<String> keySet);
}
