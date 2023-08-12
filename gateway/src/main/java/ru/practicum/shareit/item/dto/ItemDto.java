package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.validation.marker.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private int id;
    @NotBlank(groups = {Create.class}, message = "Name у Item не может быть пустым")
    private String name;
    @NotBlank(groups = {Create.class}, message = "Description не может быть пустым")
    private String description;
    @NotNull(groups = {Create.class}, message = "Available у Item должен существовать")
    private Boolean available;
    private Integer requestId;
}