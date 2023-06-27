package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, int userId);

    Collection<ItemDto> getUsersAllItems(int userId);

    ItemDto getItemById(int itemId);

    ItemDto updateItem(ItemDto itemDto, int userId, int itemId);

    Collection<ItemDto> findItemsByText(String text);
}
