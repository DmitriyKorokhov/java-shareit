package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.marker.Create;
import ru.practicum.shareit.validation.marker.Update;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto addItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                           @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Добавление нового Item");
        return itemService.addItem(itemDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ItemDto updateItem(@Validated({Update.class}) @RequestBody ItemDto itemDto,
                              @PathVariable("id") int itemId,
                              @RequestHeader(USER_ID_HEADER) int ownerId) {
        log.info("Обнавление Item с id = {}", itemId);
        return itemService.updateItem(itemDto, ownerId, itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<ResponseItemDto> getUsersAllItems(@RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Вывод всех Items User с id = {}", userId);
        return itemService.getUsersAllItems(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseItemDto getItemById(@PathVariable("id") int itemId,
                                       @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение Item с id = {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public Collection<ResponseItemDto> findItemsByText(@RequestParam String text) {
        log.info("Поиск вещи: " + text);
        return itemService.findItemsByText(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{itemId}/comment")
    public ResponseCommentDto addCommentForItem(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable int itemId,
                                                @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Добавление комментария для Item с id = {}", itemId);
        return itemService.addCommentForItem(commentDto, itemId, userId);
    }
}
