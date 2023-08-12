package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ItemRequestResponseDto {
    private int id;
    private String description;
    private List<ItemRequestDto> items;
    private LocalDateTime created;

    public List<ItemRequestDto> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }
}