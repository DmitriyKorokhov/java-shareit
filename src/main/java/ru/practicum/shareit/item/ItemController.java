package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.validation.marker.Create;
import ru.practicum.shareit.validation.marker.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.shareit.permanentunits.Constants.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

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
        return itemService.updateItem(ItemMapper.toItem(itemDto, itemId), ownerId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<ItemResponseDto> getUsersAllItems(@RequestHeader(USER_ID_HEADER) int userId,
                                                        @RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                        @PositiveOrZero int from,
                                                        @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                        @Positive int size) {
        log.info("Вывод всех Items User с id = {}", userId);
        return itemService.getUsersAllItems(userId, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ItemResponseDto getItemForUser(@PathVariable("id") int itemId, @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение Item с id = {}", itemId);
        return itemService.getItemForUser(itemId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public Collection<ItemResponseDto> findItemsByText(@RequestParam String text,
                                                       @RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                       @PositiveOrZero int from,
                                                       @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                       @Positive int size) {
        log.info("Поиск вещи: {}", text);
        return itemService.findItemsByText(text, from, size);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addCommentForItem(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable int itemId,
                                                @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Добавление комментария для Item с id = {}", itemId);
        return itemService.addCommentForItem(commentDto, itemId, userId);
    }
}