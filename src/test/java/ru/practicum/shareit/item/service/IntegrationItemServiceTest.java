package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationItemServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    private User owner;
    private User booker;
    private UserDto createdOwner;
    private UserDto createdBooker;
    private ItemDto createdItem;
    private BookingRequestDto postLastBookingDto;
    private BookingResponseDto lastBooking;
    private BookingRequestDto nextBookingDto;
    private BookingResponseDto nextBooking;
    private CommentDto commentDto;
    private List<ItemResponseDto> result;

    @BeforeEach
    void beforeEach() {
        owner = User.builder()
                .id(1)
                .name("owner")
                .email("owner@mail.com")
                .build();
        booker = User.builder()
                .id(2)
                .name("booker")
                .email("booker@mail.com")
                .build();
        createdOwner = userService.addUser(UserMapper.toUserDto(owner));
        createdBooker = userService.addUser(UserMapper.toUserDto(booker));
        createdItem = itemService.addItem(ItemDto.builder()
                        .name("item")
                        .description("description")
                        .available(true)
                        .build(),
                createdOwner.getId());
        postLastBookingDto = BookingRequestDto.builder()
                .itemId(createdItem.getId())
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();
        lastBooking = bookingService.addBooking(postLastBookingDto, createdBooker.getId());
        bookingService.approveBooking(lastBooking.getId(), true, createdOwner.getId());
        nextBookingDto = BookingRequestDto.builder()
                .itemId(createdItem.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        nextBooking = bookingService.addBooking(nextBookingDto, createdBooker.getId());
        bookingService.approveBooking(nextBooking.getId(), true, createdOwner.getId());
        commentDto = new CommentDto("comment");
        itemService.addCommentForItem(commentDto, createdItem.getId(), createdBooker.getId());

        result = itemService.getUsersAllItems(createdOwner.getId(), 0, 20);
    }

    @AfterEach
    void afterEach() {
        userService.deleteUserById(createdOwner.getId());
        userService.deleteUserById(createdBooker.getId());
    }

    @Test
    void getItemTestWhenItemListIsCorrect() {
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void getItemTestWhenItemIsCorrect() {
        ItemResponseDto itemResponseDto = result.get(0);
        assertNotNull(itemResponseDto);
        assertEquals(itemResponseDto.getId(), createdItem.getId());
        assertEquals(itemResponseDto.getName(), createdItem.getName());
        assertEquals(itemResponseDto.getAvailable(), createdItem.getAvailable());
        assertEquals(itemResponseDto.getDescription(), createdItem.getDescription());

    }

    @Test
    void getItemTestWhenCommentsAreCorrect() {
        ItemResponseDto itemResponseDto = result.get(0);
        assertNotNull(itemResponseDto.getComments());
        assertEquals(itemResponseDto.getComments().size(), 1);
    }
}