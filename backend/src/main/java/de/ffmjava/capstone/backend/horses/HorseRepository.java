package de.ffmjava.capstone.backend.horses;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HorseRepository extends MongoRepository<Horse, String> {
}
