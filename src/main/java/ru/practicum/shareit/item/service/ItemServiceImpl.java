package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, int userId) {
        User user = findUser(userId);
        Item item = ItemMapper.toItem(itemDto, userId);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int ownerId, int itemId) {
        User owner = findUser(ownerId);
        Item updateItem = ItemMapper.toItem(itemDto, itemId);
        updateItem.setOwner(owner);
        Item item = getItem(updateItem.getId());
        if (updateItem.getName() != null && !updateItem.getName().isBlank()) {
            item.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null && !updateItem.getDescription().isBlank()) {
            item.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        }
        return ItemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    public Item getItem(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Объект не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(int itemId, int userId) {
        Item item = getItem(itemId);
        List<Comment> comments = commentRepository.findByItemId(itemId);
        Booking lastBooking = null;
        Booking nextBooking = null;
        if (item.getOwner().getId() == userId) {
            lastBooking = bookingRepository.findFirstBookingByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
            nextBooking = bookingRepository.findFirstBookingByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        }
        return ItemMapper.toItemResponseDto(item, lastBooking, nextBooking, comments);
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<ItemResponseDto> getUsersAllItems(int userId) {
        User owner = findUser(userId);
        Collection<Item> items = itemRepository.findAllByOwnerOrderById(owner);
        return toResponseItemDto(items);
    }

    private Collection<ItemResponseDto> toResponseItemDto(Collection<Item> items) {
        Map<Item, List<Booking>> bookingsByItem = findApprovedBookingsByItem(items);
        Map<Item, List<Comment>> comments = findComments(items);
        return items.stream()
                .map(item -> getResponseItemDto(item, bookingsByItem.get(item), comments.get(item)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<Item, List<Comment>> findComments(Collection<Item> items) {
        return commentRepository.findByItemIn(items).stream()
                .collect(Collectors.groupingBy(Comment::getItem));
    }

    @Transactional(readOnly = true)
    public Map<Item, List<Booking>> findApprovedBookingsByItem(Collection<Item> items) {
        return bookingRepository.findBookingByItemInAndStatus(items, BookingStatus.APPROVED).stream()
                .collect(Collectors.groupingBy(Booking::getItem, Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public ItemResponseDto getResponseItemDto(Item item, List<Booking> bookings, List<Comment> comments) {
        Booking lastBooking = null;
        Booking nextBooking = null;
        if (bookings != null && !bookings.isEmpty()) {
            lastBooking = bookingRepository.findFirstBookingByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
            nextBooking = bookingRepository.findFirstBookingByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        }
        return ItemMapper.toItemResponseDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemResponseDto> findItemsByText(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.search(text);
        return toResponseItemDto(items);
    }

    @Override
    public CommentResponseDto addCommentForItem(CommentDto commentDto, int itemId, int userId) {
        Item item = getItem(itemId);
        User author = findUser(userId);
        Collection<Booking> bookings = bookingRepository.findBookingByItemIdAndBookerIdAndStatusAndStartBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        if (bookings == null || bookings.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "User некорректно задан");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, author, LocalDateTime.now());
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentResponseDto(comment);
    }

    private User findUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Объект не найден"));
    }
}