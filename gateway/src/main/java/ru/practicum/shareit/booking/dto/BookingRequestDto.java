package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.validation.customvalidation.DateValidation;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.shareit.permanentunits.Constants.DATE_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DateValidation
public class BookingRequestDto {
    @NotNull(message = "itemId должно существовать")
    private int itemId;
    @FutureOrPresent(message = "Время start не должно относиться к прошлому времени")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime start;
    @Future(message = "Время end не должно относиться к прошлому, включая настоящее, времени")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime end;
}