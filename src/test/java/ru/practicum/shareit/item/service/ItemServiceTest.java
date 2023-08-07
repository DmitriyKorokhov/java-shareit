package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestUtils.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void addItemTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(requestor));
        when(itemRequestRepository.findById(1)).thenReturn(Optional.of(request));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto itemDto = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), request.getId());
        ItemDto createdItem = itemService.addItem(itemDto, 1);

        assertNotNull(createdItem);
        assertEquals(createdItem.toString(), ItemMapper.toItemDto(item).toString());
        verify(userRepository, times(1)).findById(1);
        verify(itemRequestRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItemTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        ItemDto updateItem = itemService.updateItem(item, 1);

        assertNotNull(updateItem);
        assertEquals(updateItem.toString(), ItemMapper.toItemDto(item).toString());
        verify(userRepository, times(1)).findById(1);
        verify(itemRepository, times(1)).findById(1);
    }

    @Test
    void updateItemByNotOwnerTest() {
        int userId = 100;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> itemService.updateItem(item, userId));
    }

    @Test
    void getItemForUser() {
        User commentAuthor = new User(2, "user_name2", "user2@mail.com");
        List<Comment> comments = List.of(new Comment(1, "text", item, commentAuthor, LocalDateTime.now()));
        Booking lastBooking = Booking.builder()
                .id(1)
                .item(item)
                .booker(commentAuthor)
                .build();
        Booking nextBooking = Booking.builder()
                .id(2)
                .item(item)
                .booker(commentAuthor)
                .build();
        when(itemRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(1))
                .thenReturn(comments);
        when(bookingRepository.findFirstBookingByItemIdAndStartBeforeAndStatusOrderByStartDesc(eq(1), any(LocalDateTime.class), eq(BookingStatus.APPROVED)))
                .thenReturn(lastBooking);
        when(bookingRepository.findFirstBookingByItemIdAndStartAfterAndStatusOrderByStartAsc(eq(1), any(LocalDateTime.class), eq(BookingStatus.APPROVED)))
                .thenReturn(nextBooking);

        ItemResponseDto gottenItemDto = itemService.getItemForUser(1, 1);

        assertNotNull(gottenItemDto);
        assertEquals(gottenItemDto, ItemMapper.toResponseItemDto(item, lastBooking, nextBooking, comments));
        verify(itemRepository, times(1)).findById(any(Integer.class));
        verify(bookingRepository, times(1)).findFirstBookingByItemIdAndStartBeforeAndStatusOrderByStartDesc(eq(1), any(LocalDateTime.class), eq(BookingStatus.APPROVED));
        verify(bookingRepository, times(1)).findFirstBookingByItemIdAndStartAfterAndStatusOrderByStartAsc(eq(1), any(LocalDateTime.class), eq(BookingStatus.APPROVED));
    }

    @Test
    void getAllTest() {
        User user1 = new User(1, "user1", "user@mail.com");
        User user2 = new User(2, "user2", "user2@mail.com");
        Item item1 = Item.builder().id(1).name("item1").description("item_description1").owner(user2).build();
        Item item2 = Item.builder().id(1).name("item2").description("item_description2").owner(user2).build();
        Booking lastBooking1 = Booking.builder()
                .id(1)
                .item(item1)
                .booker(user1)
                .start(LocalDateTime.now().minusDays(1))
                .build();
        Booking nextBooking1 = Booking.builder()
                .id(2).item(item1)
                .booker(user1)
                .start(LocalDateTime.now().plusDays(1))
                .build();
        Booking lastBooking2 = Booking.builder()
                .id(3).item(item2)
                .booker(user1)
                .start(LocalDateTime.now().minusDays(1))
                .build();
        Booking nextBooking2 = Booking.builder()
                .id(4).item(item2)
                .booker(user1)
                .start(LocalDateTime.now().plusDays(1))
                .build();

        List<Comment> comments = List.of(new Comment(1, "text", item1, user1, LocalDateTime.now()));

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRepository.findAllByOwnerOrderById(user1, page))
                .thenReturn(new PageImpl<>(List.of(item1, item2)));
        when(bookingRepository.findBookingByItemInAndStatus(any(), eq(BookingStatus.APPROVED)))
                .thenReturn(List.of(lastBooking1, nextBooking1, lastBooking2, nextBooking2));
        when(commentRepository.findByItemIn(any()))
                .thenReturn(comments);

        Collection<ItemResponseDto> result = itemService.getUsersAllItems(1, 0, 20);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findById(any(Integer.class));
        verify(itemRepository, times(1)).findAllByOwnerOrderById(user1, page);
        verify(bookingRepository, times(1)).findBookingByItemInAndStatus(any(), eq(BookingStatus.APPROVED));
        verify(commentRepository, times(1)).findByItemIn(any());
    }

    @Test
    void findItemsByTextTest() {
        Collection<ItemResponseDto> emptyResult = itemService.findItemsByText("", 0, 20);
        assertTrue(emptyResult.isEmpty());
        String text = "текст";
        when(itemRepository.search(text, page))
                .thenReturn(List.of(item));
        List<ItemResponseDto> result = itemService.findItemsByText(text, 0, 20);
        verify(itemRepository, times(1)).search(text, page);
        assertNotNull(result);
        assertEquals(1, result.size());
        ItemResponseDto itemDto = ItemResponseDto.builder().id(item.getId()).name(item.getName()).description(item.getDescription())
                .available(item.getAvailable()).requestId(item.getItemRequest().getId()).build();
        assertTrue(result.contains(itemDto));
    }

    @Test
    void createCommentTest() {
        LocalDateTime now = LocalDateTime.now();
        Comment comment = Comment.builder()
                .id(1).text("text")
                .author(booker)
                .item(item)
                .created(now)
                .build();
        when(userRepository.findById(1)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(any(Integer.class))).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItemIdAndBookerIdAndStatusAndStartBefore(eq(1), eq(1), eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);

        CommentDto commentDto = new CommentDto("text");
        CommentResponseDto result = itemService.addCommentForItem(commentDto, 1, 1);

        assertNotNull(result);
        CommentResponseDto dto = CommentResponseDto.builder()
                .id(1)
                .text("text")
                .authorName(booker.getName())
                .created(now)
                .build();
        assertEquals(result, dto);
    }

    @Test
    void createItemWithUserNotFoundExceptionValidationTest() {
        int userId = 100;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> itemService.addItem(ItemMapper.toItemDto(item), userId));
        verify(itemRequestRepository, never()).findById(any());
        verify(itemRepository, never()).save(any());
    }
}