package ru.practicum.shareit.validation.customvalidation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckDateValidator.class)
public @interface DateValidation {
    String message() default "Start должно быть перед end или не равен null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
