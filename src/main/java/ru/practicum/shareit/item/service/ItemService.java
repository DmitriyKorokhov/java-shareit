package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int ownerId, int itemId);

    ResponseItemDto getItemById(int itemId, int userId);

    Collection<ResponseItemDto> getUsersAllItems(int userId);

    Collection<ResponseItemDto> findItemsByText(String text);

    ResponseCommentDto addCommentForItem(CommentDto commentDto, int itemId, int userId);
}
