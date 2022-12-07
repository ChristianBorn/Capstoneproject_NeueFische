package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.mongodb.repository.Query;



@Repository
public interface HorseRepository extends MongoRepository<Horse, String> {
    @Aggregation(pipeline = {
            "{'$unwind': {'path': '$consumptionList'}}",
            "{'$group':  {'_id': '$consumptionList.name', 'id': {'name': '$consumptionList.name', 'Verbrauch': {'$sum': '$consumptionList.dailyConsumption'}}}}"
    })
    List<Document> aggregateConsumptions();
    @Query("{ 'consumption.id':  ?0 }")
    List<Horse> findHorsesByConsumptionId(String id);
}