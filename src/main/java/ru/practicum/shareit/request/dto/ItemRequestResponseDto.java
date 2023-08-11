package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ItemRequestResponseDto {
    private int id;
    @NotBlank(message = "Description у Request должно существовать")
    private String description;
    private List<ItemRequestDto> items;
    @NotNull(message = "Время Created у Request должно существовать")
    private LocalDateTime created;

    public List<ItemRequestDto> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }
}