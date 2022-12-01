package de.ffmjava.capstone.backend.horses;

import de.ffmjava.capstone.backend.horses.model.Horse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HorseRepository extends MongoRepository<Horse, String> {
}
