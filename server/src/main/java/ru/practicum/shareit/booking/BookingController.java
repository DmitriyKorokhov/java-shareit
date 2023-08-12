package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_SIZE_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.USER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    BookingResponseDto addBooking(@RequestBody BookingRequestDto bookingRequestDto,
                                  @RequestHeader(USER_ID_HEADER) int bookerId) {
        log.info("Добавление нового Booking");
        return bookingService.addBooking(bookingRequestDto, bookerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{bookingId}")
    BookingResponseDto approveBooking(@PathVariable int bookingId, @RequestParam boolean approved,
                                      @RequestHeader(USER_ID_HEADER) int userId) {
        if (approved) {
            log.info("Подтверждение Booking с id = {}", bookingId);
        } else {
            log.info("Отклонение Booking с id = {}", bookingId);
        }
        return bookingService.approveBooking(bookingId, approved, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingForUser(@PathVariable int bookingId,
                                                @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получепние Booking с id = {}", bookingId);
        return bookingService.getBookingForUser(bookingId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<BookingResponseDto> getAllBookings(@RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                         @RequestHeader(USER_ID_HEADER) int userId,
                                                         @RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                         int from,
                                                         @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                         int size) {
        log.info("Получепние всех Booking User с id = {}", userId);
        return bookingService.getAllBookings(state, userId, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public Collection<BookingResponseDto> getAllBookingForOwner(@RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                                @RequestHeader(USER_ID_HEADER) int ownerId,
                                                                @RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                                int from,
                                                                @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                                int size) {
        log.info("Получение списка Bookings для всех вещей User с id = {}", ownerId);
        return bookingService.getAllBookingForOwner(state, ownerId, from, size);
    }
}