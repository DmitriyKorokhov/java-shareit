package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto addItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                           @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Добавление нового Item");
        return itemService.addItem(itemDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<ItemDto> getUsersAllItems(@RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Вывод всех Items User с id = {}", userId);
        return itemService.getUsersAllItems(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") int itemId) {
        log.info("Получение User с id = {}", itemId);
        return itemService.getItemById(itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable("id") int itemId,
                              @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Обнавление Item с id = {}", itemId);
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public Collection<ItemDto> findItemsByText(@RequestParam String text) {
        log.info("Запрос на поиск вещи: " + text);
        return itemService.findItemsByText(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteItemById(@PathVariable("id") int itemId) {
        log.info("Удаление Item с id = {}", itemId);
        itemService.deleteItemById(itemId);
    }
}
