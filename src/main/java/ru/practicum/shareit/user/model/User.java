package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class User {
    private Integer id;
    @NotBlank(message = "Name не должен быть пустым")
    private String name;
    @Email(message = "Email у User должен быть корректным")
    @NotBlank(message = "Email у User не должен быть пустым")
    private String email;
}
