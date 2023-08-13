package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validation.marker.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_SIZE_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.USER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> addItemRequest(@Validated({Create.class}) @RequestBody ItemRequestDto requestDto,
                                                 @RequestHeader(USER_ID_HEADER) int requestorId) {
        log.info("Добавление запроса с id = {}", requestorId);
        return itemRequestClient.addItemRequest(requestDto, requestorId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> getAllRequestsByOwner(@RequestHeader(USER_ID_HEADER) int requestorId) {
        log.info("Получение запросов пользователя с id = {}", requestorId);
        return itemRequestClient.getAllRequestsByOwner(requestorId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                 @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                 @Positive int size,
                                                 @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение запросов для пользователя с id = {}", userId);
        return itemRequestClient.getAllRequests(from, size, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable int requestId,
                                                 @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение запроса c id = {}", requestId);
        return itemRequestClient.getRequestById(requestId, userId);
    }
}