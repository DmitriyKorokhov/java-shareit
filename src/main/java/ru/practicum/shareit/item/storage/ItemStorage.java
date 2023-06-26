package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item addItem(Item item);

    Item updateItem(Item item);

    Collection<Item> getUsersAllItems(int userId);

    Item getItemById(int itemId);

    Collection<Item> findItemsByText(String text);
}
