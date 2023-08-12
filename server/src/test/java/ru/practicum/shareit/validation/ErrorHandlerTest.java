package ru.practicum.shareit.validation;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.validation.errorhandler.ErrorHandler;
import ru.practicum.shareit.validation.errorhandler.ErrorResponse;
import ru.practicum.shareit.validation.exception.IncorrectStatusException;
import ru.practicum.shareit.validation.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandlerTest {

    private final ErrorHandler handler = new ErrorHandler();

    @Test
    public void handleUnsupportedStatusExceptionTest() {
        IncorrectStatusException e = new IncorrectStatusException(HttpStatus.BAD_REQUEST, "Unknown state: UNSUPPORTED_STATUS");
        ErrorResponse errorResponse = handler.handle(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), "Unknown state: UNSUPPORTED_STATUS");
    }

    @Test
    public void handleValidationsExceptionTest() {
        ValidationException e = new ValidationException(HttpStatus.NOT_FOUND, "Unknown state: UNSUPPORTED_STATUS");
        ValidationException gottenException = assertThrows(ValidationException.class, () -> handler.handleValidationException(e));
        assertNotNull(gottenException);
        assertEquals(gottenException.getMessage(), "Unknown state: UNSUPPORTED_STATUS");
    }

    @Test
    public void handleBadRequestStatusExceptionTest() {
        ValidationException e = new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
        ErrorResponse errorResponse = handler.handleInternalServerError(e);
        assertNotNull(errorResponse);
        assertEquals(errorResponse.getDescription(), "INTERNAL_SERVER_ERROR");
    }

}
