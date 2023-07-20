package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.constant.Constants;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
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

    private final ItemService itemService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto addItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                           @RequestHeader(Constants.USER_ID_HEADER) int userId) {
        log.info("Добавление нового Item");
        return itemService.addItem(itemDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ItemDto updateItem(@Validated({Update.class}) @RequestBody ItemDto itemDto,
                              @PathVariable("id") int itemId,
                              @RequestHeader(Constants.USER_ID_HEADER) int ownerId) {
        log.info("Обнавление Item с id = {}", itemId);
        return itemService.updateItem(itemDto, ownerId, itemId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Collection<ItemResponseDto> getUsersAllItems(@RequestHeader(Constants.USER_ID_HEADER) int userId) {
        log.info("Вывод всех Items User с id = {}", userId);
        return itemService.getUsersAllItems(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ItemResponseDto getItemById(@PathVariable("id") int itemId,
                                       @RequestHeader(Constants.USER_ID_HEADER) int userId) {
        log.info("Получение Item с id = {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public Collection<ItemResponseDto> findItemsByText(@RequestParam String text) {
        log.info("Поиск вещи: " + text);
        return itemService.findItemsByText(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addCommentForItem(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable int itemId,
                                                @RequestHeader(Constants.USER_ID_HEADER) int userId) {
        log.info("Добавление комментария для Item с id = {}", itemId);
        return itemService.addCommentForItem(commentDto, itemId, userId);
    }
}
