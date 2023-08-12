package ru.practicum.shareit.validation.errorhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.validation.exception.IncorrectStatusException;
import ru.practicum.shareit.validation.exception.ValidationException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IncorrectStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(IncorrectStatusException e) {
        log.error(e.getMessage(), e, HttpStatus.resolve(400));
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowable(ConstraintViolationException e) {
        log.error(e.getMessage(), e, HttpStatus.resolve(400));
        return new ErrorResponse("Bad Request", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(Throwable e) {
        log.error(e.getMessage(), e, HttpStatus.resolve(500));
        return new ErrorResponse("Internal Server Error", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDateException(ConstraintViolationException e) {
        log.error(e.getMessage(), HttpStatus.resolve(400));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ValidationException handleValidationException(final ValidationException e) {
        log.error(e.getMessage(), e);
        throw e;
    }
}