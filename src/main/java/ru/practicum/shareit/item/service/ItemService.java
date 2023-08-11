package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(Item item, int ownerId);

    ItemResponseDto getItemForUser(int itemId, int userId);

    List<ItemResponseDto> getUsersAllItems(int userId, int from, int size);

    List<ItemResponseDto> findItemsByText(String text, int from, int size);

    CommentResponseDto addCommentForItem(CommentDto commentDto, int itemId, int userId);
}