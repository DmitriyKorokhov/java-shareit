package ru.practicum.shareit.booking.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.TestUtils.bookerWithoutId;
import static ru.practicum.shareit.TestUtils.ownerWithoutId;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private User booker;
    private User itemOwner;
    private Booking booking;
    private LocalDateTime end;
    private LocalDateTime start;
    private BookingStatus bookingStatus;

    @BeforeEach
    public void beforeEach() {
        start = LocalDateTime.now().plusDays(1);
        end = start.plusDays(7);
        bookingStatus = BookingStatus.APPROVED;
        itemOwner = userRepository.save(ownerWithoutId);
        booker = userRepository.save(bookerWithoutId);
        item = itemRepository.save(Item.builder()
                .owner(itemOwner)
                .name("item")
                .description("description")
                .available(true)
                .build()
        );
        booking = bookingRepository.save(Booking.builder().booker(booker).status(BookingStatus.APPROVED)
                .item(item).start(start).end(end).build());
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void findByBookerIdCurrentTest() {
        Page<Booking> result = bookingRepository
                .findByBookerIdCurrent(booker.getId(), start.plusDays(3), Pageable.unpaged());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findByBookerIdAndEndIsBeforeTest() {
        Page<Booking> result = bookingRepository
                .findByBookerIdAndEndIsBefore(booker.getId(), LocalDateTime.now().plusDays(10), Pageable.unpaged());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findByBookerIdAndStartIsAfterTest() {
        Page<Booking> result = bookingRepository
                .findByBookerIdAndStartIsAfter(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findByBookerIdAndStatusTest() {
        Page<Booking> result = bookingRepository
                .findByBookerIdAndStatus(booker.getId(), bookingStatus, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findBookingsByItemOwnerCurrentTest() {
        Page<Booking> result = bookingRepository
                .findBookingsByItemOwnerCurrent(itemOwner, end.minusDays(5), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findBookingByItemOwnerAndEndIsBeforeTest() {
        Page<Booking> result = bookingRepository
                .findBookingByItemOwnerAndEndIsBefore(
                        itemOwner, LocalDateTime.now().plusDays(30), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findBookingByItemOwnerAndStartIsAfterTest() {
        Page<Booking> result = bookingRepository
                .findBookingByItemOwnerAndStartIsAfter(itemOwner, LocalDateTime.now(), Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findBookingByItemOwnerAndStatusTest() {
        Page<Booking> result = bookingRepository
                .findBookingByItemOwnerAndStatus(itemOwner, bookingStatus, Pageable.unpaged());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findBookingByItemOwnerTest() {
        Page<Booking> result = bookingRepository
                .findBookingByItemOwner(itemOwner, Pageable.unpaged());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findBookingByItemIdAndStartAfterBefore() {
        Booking result = bookingRepository
                .findFirstBookingByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), LocalDateTime.now().plusDays(3), bookingStatus);
        assertNotNull(result);
        assertEquals(booking, result);
    }

    @Test
    void findBookingByItemIdAndStartAfterAndStatusTest() {
        Booking result = bookingRepository
                .findFirstBookingByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(), bookingStatus);
        assertNotNull(result);
        assertEquals(booking, result);
    }

    @Test
    void findByBookerIdTest() {
        Page<Booking> result = bookingRepository.findByBookerId(booker.getId(), Pageable.unpaged());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.getContent().get(0));
    }

    @Test
    void findBookingByItemIdAndBookerIdAndStatusAndStartBeforeTest() {
        List<Booking> result = bookingRepository.findBookingByItemIdAndBookerIdAndStatusAndStartBefore(item.getId(), booker.getId(), bookingStatus, LocalDateTime.now().plusDays(3));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.get(0));
    }

    @Test
    void findBookingByItemInAndStatusTest() {
        List<Booking> result = bookingRepository.findBookingByItemInAndStatus(List.of(item), bookingStatus);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(booking, result.get(0));
    }
}