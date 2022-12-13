package de.ffmjava.capstone.backend.model;

public record FormError(
        String errorMessage,
        String fieldName
) {
}
