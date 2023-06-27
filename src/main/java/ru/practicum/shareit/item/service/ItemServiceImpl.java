package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
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
    public ItemDto addItem(ItemDto itemDto, int userId) {
        Item item = ItemMapper.toItem(itemDto, userId);
        userStorage.checkUserById(item.getOwnerId());
        return ItemMapper.toItemDto(itemStorage.addItem(item));
    }

    @Override
    public Collection<ItemDto> getUsersAllItems(int userId) {
        userStorage.checkUserById(userId);
        return ItemMapper.toListOfItemDto(itemStorage.getUsersAllItems(userId));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return ItemMapper.toItemDto(itemStorage.getItemById(itemId));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int userId, int itemId) {
        return ItemMapper.toItemDto(itemStorage.updateItem(ItemMapper.toItem(itemDto, userId, itemId)));
    }

    @Override
    public Collection<ItemDto> findItemsByText(String text) {
        return ItemMapper.toListOfItemDto(itemStorage.findItemsByText(text));
    }

    @Override
    public void deleteItemById(int itemId) {
        itemStorage.deleteItemById(itemId);
    }
}
