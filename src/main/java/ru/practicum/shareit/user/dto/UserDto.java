package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.marker.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private int id;
    @NotNull(groups = {Create.class}, message = "Name у User должен существовать")
    private final String name;
    @Email(groups = {Create.class}, message = "Email у User должен быть корректным")
    @NotNull(groups = {Create.class}, message = "Email у User должен существовать")
    private final String email;
}
