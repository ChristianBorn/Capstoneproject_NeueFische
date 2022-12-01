package de.ffmjava.capstone.backend.user;


import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
public class CustomApiErrorHandler {

    private Map<String, ArrayList<String>> fieldMessageMappings = new HashMap<>();
    private String jsonString;
    private Errors errors;

    public CustomApiErrorHandler(Errors errors) {
        for (FieldError singleError : errors.getFieldErrors()) {
            if (!fieldMessageMappings.containsKey(singleError.getField())) {
                fieldMessageMappings.put(singleError.getField(), new ArrayList<>());
            }
            fieldMessageMappings.get(singleError.getField()).add(singleError.getDefaultMessage());
            Collections.sort(fieldMessageMappings.get(singleError.getField()));
        }
        this.errors = errors;
    }

    @Nullable
    public static ResponseEntity<Object> handlePossibleErrors(Errors errors) {
        if (errors.hasErrors()) {
            FieldError fieldError;
            String errorMessage = null;
            if (errors.getFieldError() != null) {
                fieldError = errors.getFieldError();
                if (fieldError != null) {
                    errorMessage = fieldError.getDefaultMessage();
                }
            }
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}
