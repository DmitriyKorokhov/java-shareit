package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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

    @Test
    void addBookingTest() {
        UserDto userDto1 = new UserDto(1, "owner", "owner@mail.com");
        UserDto userDto2 = new UserDto(2, "booker", "booker@mail.com");
        UserDto createdOwner = userService.addUser(userDto1);
        UserDto createdBooker = userService.addUser(userDto2);
        ItemDto createdItem = itemService.addItem(ItemDto.builder().name("item1").description("description1")
                .available(true).build(), createdOwner.getId());
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .itemId(createdItem.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        BookingResponseDto booking = bookingService.addBooking(bookingRequestDto, createdBooker.getId());
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM bookings AS b WHERE b.id = :bookingId", Booking.class);
        Booking gottenBooking = query.setParameter("bookingId", booking.getId()).getSingleResult();

        assertThat(gottenBooking.getId(), equalTo(booking.getId()));
        assertThat(gottenBooking.getStatus(), equalTo(booking.getStatus()));
        assertThat(gottenBooking.getStart(), equalTo(booking.getStart()));
        assertThat(gottenBooking.getEnd(), equalTo(booking.getEnd()));

        userService.deleteUserById(createdOwner.getId());
        userService.deleteUserById(createdBooker.getId());
    }
}