package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.marker.Create;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                           @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Новый Item добавлен");
        Item item = itemService.addItem(ItemMapper.toItem(itemDto, userId));
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable("id") int itemId,
                              @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Item с id = " + itemId + " обновлен");
        Item item = itemService.updateItem(ItemMapper.toItem(itemDto, userId, itemId));
        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    public Collection<ItemDto> getUsersAllItems(@RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Вывод всех Items User с id = " + userId);
        Collection<Item> items = itemService.getUsersAllItems(userId);
        return ItemMapper.toItemDto(items);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") int itemId) {
        log.info("User с id = " + itemId + " получен");
        Item item = itemService.getItemById(itemId);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItemsByText(@RequestParam String text) {
        log.info("Запрос на поиск вещи: " + text);
        Collection<Item> items = itemService.findItemsByText(text);
        return ItemMapper.toItemDto(items);
    }
}
