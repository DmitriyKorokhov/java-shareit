package ru.practicum.shareit.validation.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.validation.exception.ValidationException;
import ru.practicum.shareit.validation.exception.UserDataException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleValidation(final ValidationException e) {
        log.warn("Ошибка - переданные данные объекта некорректны", HttpStatus.resolve(404));
        return Map.of(
                "Ошибка валидации", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(final UserDataException e) {
        log.warn("Конфликт - переданные данные объекта некорректны", HttpStatus.resolve(409));
        return Map.of(
                "Ошибка в данных пользователя", e.getMessage()
        );
    }
}
