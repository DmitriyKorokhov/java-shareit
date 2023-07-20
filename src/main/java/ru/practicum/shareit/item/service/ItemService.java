package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int ownerId, int itemId);

    ItemResponseDto getItemById(int itemId, int userId);

    Collection<ItemResponseDto> getUsersAllItems(int userId);

    Collection<ItemResponseDto> findItemsByText(String text);

    CommentResponseDto addCommentForItem(CommentDto commentDto, int itemId, int userId);
}