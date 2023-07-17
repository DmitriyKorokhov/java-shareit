package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.exception.IncorrectStatusException;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private static final Sort SORT_BY_START_DESC = Sort.by("start").descending();
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDto addBooking(BookingRequestDto bookingRequestDto, int userId) {
        Item item = findItem(bookingRequestDto.getItemId());
        if (item.getOwner().getId() == userId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "User некорректно задан");
        }
        if (!item.getAvailable()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Item недоступна");
        }
        if (bookingRequestDto.getStart().isAfter(bookingRequestDto.getEnd()) || bookingRequestDto.getStart().isEqual(bookingRequestDto.getEnd())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Time некорректно задано");
        }
        User user = findUser(userId);
        Booking booking = BookingMapper.toBooking(bookingRequestDto, item, user);
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto approveBooking(int bookingId, boolean approved, int userId) {
        Booking booking = getBooking(bookingId);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "User некорректно задан");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Booking уже было подтверждено");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBooking(int bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Объект не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingForUser(int bookingId, int userId) {
        Booking booking = getBooking(bookingId);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "User некорректно задан");
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingResponseDto> getAllBookings(BookingState state, int userId) {
        findUser(userId);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case CURRENT:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findByBookerIdCurrent(userId, now, SORT_BY_START_DESC)
                );
            case PAST:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findByBookerIdAndEndIsBefore(userId, now, SORT_BY_START_DESC)
                );
            case FUTURE:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findByBookerIdAndStartIsAfter(userId, now, SORT_BY_START_DESC)
                );
            case WAITING:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, SORT_BY_START_DESC)
                );
            case REJECTED:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, SORT_BY_START_DESC)
                );
            case UNSUPPORTED_STATUS:
                throw new IncorrectStatusException("Unknown state: UNSUPPORTED_STATUS");
            case ALL:
            default:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findByBookerId(userId, SORT_BY_START_DESC)
                );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingResponseDto> getAllBookingForOwner(BookingState state, int ownerId) {
        User owner = findUser(ownerId);
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case CURRENT:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findBookingsByItemOwnerCurrent(owner, now, SORT_BY_START_DESC)
                );
            case PAST:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findBookingByItemOwnerAndEndIsBefore(owner, now, SORT_BY_START_DESC)
                );
            case FUTURE:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findBookingByItemOwnerAndStartIsAfter(owner, now, SORT_BY_START_DESC)
                );
            case WAITING:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findBookingByItemOwnerAndStatus(owner, BookingStatus.WAITING, SORT_BY_START_DESC)
                );
            case REJECTED:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findBookingByItemOwnerAndStatus(owner, BookingStatus.REJECTED, SORT_BY_START_DESC)
                );
            case UNSUPPORTED_STATUS:
                throw new IncorrectStatusException("Unknown state: UNSUPPORTED_STATUS");
            case ALL:
            default:
                return BookingMapper.toListBookingDto(
                        bookingRepository.findBookingByItemOwner(owner, SORT_BY_START_DESC)
                );
        }
    }

    private Item findItem(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Объект не найден"));
    }

    private User findUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Объект не найден"));
    }
}