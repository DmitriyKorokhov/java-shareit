package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item addItem(Item item);

    Item updateItem(Item item);

    Item getItemById(int itemId);

    Collection<Item> getUsersAllItems(int userId);

    Collection<Item> findItemsByText(String text);
}
