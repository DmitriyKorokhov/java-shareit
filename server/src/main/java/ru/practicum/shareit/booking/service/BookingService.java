package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingResponseDto addBooking(BookingRequestDto bookingRequestDto, int userId);

    BookingResponseDto approveBooking(int bookingId, boolean approved, int userId);

    Booking getBookingById(int bookingId);

    BookingResponseDto getBookingForUser(int bookingId, int userId);

    Collection<BookingResponseDto> getAllBookings(BookingState state, int userId, int from, int size);

    Collection<BookingResponseDto> getAllBookingForOwner(BookingState state, int ownerId, int from, int size);
}
