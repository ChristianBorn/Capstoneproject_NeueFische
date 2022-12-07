package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorseRepository extends MongoRepository<Horse, String> {
    @Query("{ 'consumption.id':  ?0}")
    List<Horse> findHorsesByConsumptionId(String id);
}
