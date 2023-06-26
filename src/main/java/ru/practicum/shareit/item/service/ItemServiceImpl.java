package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Item addItem(Item item) {
        userStorage.checkUserById(item.getOwner());
        return itemStorage.addItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        return itemStorage.updateItem(item);
    }

    @Override
    public Item getItemById(int itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public Collection<Item> getUsersAllItems(int userId) {
        userStorage.checkUserById(userId);
        return itemStorage.getUsersAllItems(userId);
    }

    @Override
    public Collection<Item> findItemsByText(String text) {
        return itemStorage.findItemsByText(text);
    }
}
