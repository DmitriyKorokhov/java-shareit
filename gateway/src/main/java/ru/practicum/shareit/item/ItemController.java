package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.marker.Create;
import ru.practicum.shareit.validation.marker.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.permanentunits.Constants.*;

@Slf4j
@Validated
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                                          @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Добавление нового Item");
        return itemClient.addItem(itemDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<Object> updateItem(@Validated({Update.class}) @RequestBody ItemDto itemDto,
                                             @PathVariable("id") int itemId,
                                             @RequestHeader(USER_ID_HEADER) int ownerId) {
        log.info("Обнавление Item с id = {}", itemId);
        return itemClient.updateItem(itemDto, itemId, ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getUsersAllItems(@RequestHeader(USER_ID_HEADER) int userId,
                                                   @RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                   @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                   @Positive int size) {
        log.info("Вывод всех Items User с id = {}", userId);
        return itemClient.getUsersAllItems(userId, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<Object> getItemById(@PathVariable("id") int itemId, @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение Item с id = {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Object> findItemsByText(@RequestParam String text,
                                                  @RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                  @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                  @Positive int size) {
        log.info("Поиск вещи: {}", text);
        return itemClient.findItemsByText(text, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentForItem(@Valid @RequestBody CommentDto commentDto,
                                                    @PathVariable int itemId,
                                                    @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Добавление комментария для Item с id = {}", itemId);
        return itemClient.addCommentForItem(commentDto, itemId, userId);
    }
}