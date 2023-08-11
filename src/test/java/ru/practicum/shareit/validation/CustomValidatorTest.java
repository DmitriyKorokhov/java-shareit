package ru.practicum.shareit.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.practicum.shareit.validation.customvalidation.DateValidation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomValidatorTest {

    private Validator validator;

    @BeforeAll
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static class TestObject {

        @DateValidation
        private String testField;

        TestObject() {
            this(null);
        }

        TestObject(String value) {
            testField = value;
        }
    }

    @Test
    void shouldValidForNullValue() {
        var obj = new TestObject();
        var violations = validator.validate(obj);
        assertTrue(violations.isEmpty(), String.format("Start должно быть перед end или не равен null", violations.size()));
    }
}