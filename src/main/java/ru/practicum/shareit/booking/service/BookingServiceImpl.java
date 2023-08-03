package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.permanentunits.ShareitPageRequest;
import ru.practicum.shareit.validation.exception.IncorrectStatusException;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.shareit.permanentunits.Sorts.SORT_BY_START_DESC;

@RequiredArgsConstructor
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDto addBooking(BookingRequestDto bookingRequestDto, int userId) {
        Item item = findItem(bookingRequestDto.getItemId());
        if (item.getOwner().getId() == userId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Некорректно задан пользователь");
        }
        if (!item.getAvailable()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Вещь недоступна");
        }
        User user = findUser(userId);
        Booking booking = BookingMapper.toBooking(bookingRequestDto, item, user);
        return BookingMapper.toResponseBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto approveBooking(int bookingId, boolean approved, int userId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Некорректно задан пользователь");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Бронирование уже подтверждено или отклонено");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toResponseBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(int bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Ресурс не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingForUser(int bookingId, int userId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "Некорректно задан пользователь");
        }
        return BookingMapper.toResponseBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingResponseDto> getAllBookings(BookingState state, int userId, int from, int size) {
        findUser(userId);
        LocalDateTime now = LocalDateTime.now();
        Pageable page = new ShareitPageRequest(from, size, SORT_BY_START_DESC);
        switch (state) {
            case CURRENT:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findByBookerIdCurrent(userId, now, page).toList());
            case PAST:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findByBookerIdAndEndIsBefore(userId, now, page).toList());
            case FUTURE:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findByBookerIdAndStartIsAfter(userId, now, page).toList());
            case WAITING:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, page).toList());
            case REJECTED:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, page).toList());
            case UNSUPPORTED_STATUS:
                throw new IncorrectStatusException(HttpStatus.BAD_REQUEST, "Unknown state: UNSUPPORTED_STATUS");
            case ALL:
            default:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findByBookerId(userId, page).toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingResponseDto> getAllBookingForOwner(BookingState state, int ownerId, int from, int size) {
        User owner = findUser(ownerId);
        LocalDateTime now = LocalDateTime.now();
        Pageable page = new ShareitPageRequest(from, size, SORT_BY_START_DESC);
        switch (state) {
            case CURRENT:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findBookingsByItemOwnerCurrent(owner, now, page).toList());
            case PAST:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findBookingByItemOwnerAndEndIsBefore(owner, now, page).toList());
            case FUTURE:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findBookingByItemOwnerAndStartIsAfter(owner, now, page).toList());
            case WAITING:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findBookingByItemOwnerAndStatus(owner, BookingStatus.WAITING, page).toList());
            case REJECTED:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findBookingByItemOwnerAndStatus(owner, BookingStatus.REJECTED, page).toList());
            case UNSUPPORTED_STATUS:
                throw new IncorrectStatusException(HttpStatus.BAD_REQUEST, "Unknown state: UNSUPPORTED_STATUS");
            case ALL:
            default:
                return BookingMapper.toBookingReferencedDto(bookingRepository.findBookingByItemOwner(owner, page).toList());
        }
    }

    private Item findItem(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Ресурс не найден"));
    }

    private User findUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Ресурс не найден"));
    }
}