package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemResponseDto {
    private int id;
    @NotBlank(message = "Name у Item не может быть пустым")
    private String name;
    @NotBlank(message = "Description не может быть пустым")
    private String description;
    @NotNull(message = "Available у Item не может быть пустым")
    private Boolean available;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private List<CommentResponseDto> comments;
}