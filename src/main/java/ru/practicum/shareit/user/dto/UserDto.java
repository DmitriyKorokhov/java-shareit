package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.marker.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    @NotNull(groups = {Create.class}, message = "Name у User должен существовать")
    private String name;
    @Email(groups = {Create.class}, message = "Email у User должен быть корректным")
    @NotNull(groups = {Create.class}, message = "Email у User должен существовать")
    private String email;
}