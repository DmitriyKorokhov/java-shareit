package ru.practicum.shareit.validation.exception;

import org.springframework.http.HttpStatus;

public class IncorrectStatusException extends ValidationException {
    public IncorrectStatusException(HttpStatus status, String message) {
        super(status, message);
    }
}
