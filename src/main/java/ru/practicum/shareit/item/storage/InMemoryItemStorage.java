package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> mapItems = new HashMap<>();
    private final Map<Integer, List<Item>> mapItemsByUser = new HashMap<>();
    private int itemId = 0;

    private void checkItemById(int id) {
        if (!mapItems.containsKey(id)) {
            throw new ValidationException("Item с id = " + id + "не существует");
        }
    }

    @Override
    public Item addItem(Item item) {
        item.setId(++itemId);
        mapItems.put(item.getId(), item);
        mapItemsByUser.compute(item.getOwner(), (userId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item updateItem(Item updatedItem) {
        int id = updatedItem.getId();
        checkItemById(id);
        Item item = mapItems.get(id);
        if (item.getOwner() != updatedItem.getOwner()) {
            throw new ValidationException("Неизвестный User у Item c id = " + id);
        }
        if (updatedItem.getName() != null && !updatedItem.getName().isBlank()) {
            item.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null && !updatedItem.getDescription().isBlank()) {
            item.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }
        return item;
    }

    @Override
    public Collection<Item> getUsersAllItems(int userId) {
        if (!mapItemsByUser.containsKey(userId)) {
            return Collections.emptyList();
        }
        return mapItemsByUser.get(userId);
    }

    @Override
    public Item getItemById(int itemId) {
        checkItemById(itemId);
        return mapItems.get(itemId);
    }

    @Override
    public Collection<Item> findItemsByText(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        return mapItems.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        item.getAvailable())
                .collect(Collectors.toList());
    }
}
