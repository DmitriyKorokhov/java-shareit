package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingItemDto {
    private int id;
    @NotNull(message = "Время start у Booking не может быть пустым")
    private LocalDateTime start;
    @NotNull(message = "Время end у Booking не может быть пустым")
    private LocalDateTime end;
    private int itemId;
    private int bookerId;
    private BookingStatus status;
}