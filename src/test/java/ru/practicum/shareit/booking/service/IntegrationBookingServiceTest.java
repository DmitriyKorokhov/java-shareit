package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationBookingServiceTest {

    private final EntityManager em;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;

    private UserDto userDto1;
    private UserDto userDto2;
    private UserDto createdOwner;
    private UserDto createdBooker;
    private ItemDto createdItem;
    private BookingRequestDto bookingRequestDto;
    private BookingResponseDto booking;
    private TypedQuery<Booking> query;
    private Booking gottenBooking;

    @BeforeEach
    void init() {
        userDto1 = new UserDto(1, "owner", "owner@mail.com");
        userDto2 = new UserDto(2, "booker", "booker@mail.com");
        createdOwner = userService.addUser(userDto1);
        createdBooker = userService.addUser(userDto2);
        createdItem = itemService.addItem(ItemDto.builder().name("item1").description("description1")
                .available(true).build(), createdOwner.getId());
        bookingRequestDto = BookingRequestDto.builder()
                .itemId(createdItem.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        booking = bookingService.addBooking(bookingRequestDto, createdBooker.getId());
        query = em.createQuery("SELECT b FROM bookings AS b WHERE b.id = :bookingId", Booking.class);
        gottenBooking = query.setParameter("bookingId", booking.getId()).getSingleResult();
    }

    @AfterEach
    void teardown() {
        userService.deleteUserById(createdOwner.getId());
        userService.deleteUserById(createdBooker.getId());
    }

    @Test
    void addBookingTestWhenIdIsCorrect() {
        assertThat(gottenBooking.getId(), equalTo(booking.getId()));
    }

    @Test
    void addBookingTestWhenStatusIsCorrect() {
        assertThat(gottenBooking.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void addBookingTestWhenStartAndEndIsCorrect() {
        assertThat(gottenBooking.getStart(), equalTo(booking.getStart()));
        assertThat(gottenBooking.getEnd(), equalTo(booking.getEnd()));
    }
}