package de.ffmjava.capstone.backend.user;

public class UserAlreadyExistsException extends RuntimeException {


    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
