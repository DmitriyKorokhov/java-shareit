package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Item {
    private int id;
    @NotBlank(message = "Name не может быть пустым")
    private String name;
    @NotBlank(message = "Description не может быть пустым")
    private String description;
    @NotNull(message = "Available не может быть пустым")
    private Boolean available;
    @NotNull(message = "У Item должен быть Owner")
    private int owner;
    private ItemRequest request;
}
