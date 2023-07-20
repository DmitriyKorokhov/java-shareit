package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.constant.Constants;
import ru.practicum.shareit.validation.customvalidation.DateValidation;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@DateValidation
public class BookingRequestDto {
    private int itemId;
    @FutureOrPresent(message = "Время start не должно относиться к прошлому времени")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.PATTERN_DATE)
    private LocalDateTime start;
    @Future(message = "Время end не должно относиться к прошлому, включая настоящее, времени")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.PATTERN_DATE)
    private LocalDateTime end;
}
