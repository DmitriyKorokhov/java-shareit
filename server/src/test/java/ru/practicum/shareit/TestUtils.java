package ru.practicum.shareit;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.permanentunits.ShareitPageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class TestUtils {

    public static final Pageable page = new ShareitPageRequest();
    public static final LocalDateTime now = LocalDateTime.now().plusHours(1);

    public static final User owner = User.builder()
            .id(1)
            .name("owner")
            .email("owner@mail.com")
            .build();

    public static final User booker = User.builder()
            .id(2)
            .name("booker")
            .email("booker@mail.com")
            .build();

    public static final User requestor = User.builder()
            .id(3)
            .name("requestor")
            .email("requestor@mail.com")
            .build();

    public static final ItemRequest request = ItemRequest.builder()
            .id(1)
            .requestor(requestor)
            .description("description")
            .created(LocalDateTime.now())
            .build();

    public static final Item item = Item.builder()
            .id(1).name("item")
            .description("description")
            .available(true)
            .owner(owner)
            .itemRequest(request)
            .build();

    public static final Booking booking = Booking.builder()
            .id(1)
            .item(item)
            .start(now)
            .end(now.plusDays(1))
            .booker(booker)
            .status(BookingStatus.WAITING)
            .build();

    public static final User ownerWithoutId = User.builder()
            .name("owner")
            .email("owner@mail.com")
            .build();

    public static final User bookerWithoutId = User.builder()
            .name("booker")
            .email("booker@mail.com")
            .build();

    public static final User requestorWithoutId = User.builder()
            .name("requestor")
            .email("requestor@mail.com")
            .build();
}