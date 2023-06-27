package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class User {
    private Integer id;
    @NotNull(message = "Name у User должен существовать")
    private String name;
    @Email(message = "Email у User должен быть корректным")
    private String email;
}
