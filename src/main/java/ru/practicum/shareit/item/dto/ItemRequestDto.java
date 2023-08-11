package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemRequestDto {
    private int id;
    @NotBlank(message = "Name у Item не может быть пустым")
    private String name;
    @NotBlank(message = "Description не может быть пустым")
    private String description;
    @NotNull(message = "Available у Item не может быть пустым")
    private boolean available;
    private int requestId;
}
