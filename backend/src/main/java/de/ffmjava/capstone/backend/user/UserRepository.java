package de.ffmjava.capstone.backend.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<AppUser, String> {
    AppUser findByUsername(String username);

}
