package ru.practicum.shareit.validation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {

    private final String message;

    public ValidationException(HttpStatus status, String message) {
        super(status, message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}