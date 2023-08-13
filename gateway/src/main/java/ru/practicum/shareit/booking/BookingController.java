package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_SIZE_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.USER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ResponseEntity<Object> addBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto,
                                      @RequestHeader(USER_ID_HEADER) int bookerId) {
        log.info("Добавление нового Booking");
        return bookingClient.addBooking(bookingRequestDto, bookerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{bookingId}")
    ResponseEntity<Object> approveBooking(@PathVariable int bookingId, @RequestParam boolean approved,
                                          @RequestHeader(USER_ID_HEADER) int userId) {
        if (approved) {
            log.info("Подтверждение Booking с id = {}", bookingId);
        } else {
            log.info("Отклонение Booking с id = {}", bookingId);
        }
        return bookingClient.approveBooking(bookingId, userId, approved);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable int bookingId,
                                                 @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получепние Booking с id = {}", bookingId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getAllBookings(@RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                 @RequestHeader(USER_ID_HEADER) int userId,
                                                 @RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                 @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                 @Positive int size) {
        log.info("Получепние всех Booking User с id = {}", userId);
        return bookingClient.getAllBookings(userId, state, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingForOwner(@RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                        @RequestHeader(USER_ID_HEADER) int ownerId,
                                                        @RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                        @PositiveOrZero int from,
                                                        @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                        @Positive int size) {
        log.info("Получение списка Bookings для всех вещей User с id = {}", ownerId);
        return bookingClient.getAllBookingForOwner(ownerId, state, from, size);
    }
}