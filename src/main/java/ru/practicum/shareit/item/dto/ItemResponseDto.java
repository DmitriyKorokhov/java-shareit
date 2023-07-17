package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemResponseDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private List<ResponseCommentDto> comments;
}
