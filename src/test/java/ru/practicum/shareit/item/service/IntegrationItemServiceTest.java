package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.practicum.shareit.TestUtils.bookerWithoutId;
import static ru.practicum.shareit.TestUtils.ownerWithoutId;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationItemServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    void getAllTest() {
        UserDto createdOwner = userService.addUser(UserMapper.toUserDto(ownerWithoutId));
        UserDto createdBooker = userService.addUser(UserMapper.toUserDto(bookerWithoutId));
        ItemDto createdItem = itemService.addItem(ItemDto.builder()
                        .name("item")
                        .description("description")
                        .available(true)
                        .build(),
                createdOwner.getId());
        BookingRequestDto postLastBookingDto = BookingRequestDto.builder()
                .itemId(createdItem.getId())
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();
        BookingResponseDto lastBooking = bookingService.addBooking(postLastBookingDto, createdBooker.getId());
        bookingService.approveBooking(lastBooking.getId(), true, createdOwner.getId());
        BookingRequestDto nextBookingDto = BookingRequestDto.builder()
                .itemId(createdItem.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        BookingResponseDto nextBooking = bookingService.addBooking(nextBookingDto, createdBooker.getId());
        bookingService.approveBooking(nextBooking.getId(), true, createdOwner.getId());
        CommentDto commentDto = new CommentDto("comment");
        itemService.addCommentForItem(commentDto, createdItem.getId(), createdBooker.getId());

        List<ItemResponseDto> result = itemService.getUsersAllItems(createdOwner.getId(), 0, 20);
        assertNotNull(result);
        assertEquals(result.size(), 1);

        ItemResponseDto itemResponseDto = result.get(0);
        assertNotNull(itemResponseDto);
        assertEquals(itemResponseDto.getId(), createdItem.getId());
        assertEquals(itemResponseDto.getName(), createdItem.getName());
        assertEquals(itemResponseDto.getAvailable(), createdItem.getAvailable());
        assertEquals(itemResponseDto.getDescription(), createdItem.getDescription());
        assertNotNull(itemResponseDto.getComments());
        assertEquals(itemResponseDto.getComments().size(), 1);

        userService.deleteUserById(createdOwner.getId());
        userService.deleteUserById(createdBooker.getId());
    }
}
