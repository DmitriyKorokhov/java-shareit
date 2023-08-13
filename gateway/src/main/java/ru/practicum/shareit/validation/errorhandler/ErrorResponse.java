package ru.practicum.shareit.validation.errorhandler;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String description;
}