package de.ffmjava.capstone.backend.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
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
        try {
            this.jsonString = new ObjectMapper().writeValueAsString(fieldMessageMappings);
        } catch (JsonProcessingException e) {
            return;
        }
        this.errors = errors;
    }

}
