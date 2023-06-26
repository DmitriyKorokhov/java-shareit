package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@Builder
public class Item {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private int owner;
    private ItemRequest request;
}
