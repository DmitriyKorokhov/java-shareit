package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.marker.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    @NotBlank(groups = {Create.class}, message = "Name у User не должен быть пустым")
    private String name;
    @Email(groups = {Create.class}, message = "Email у User должен быть корректным")
    @NotNull(groups = {Create.class}, message = "Email у User должен существовать")
    private String email;
}