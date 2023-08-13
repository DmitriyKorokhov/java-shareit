package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_SIZE_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.USER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemRequestResponseDto addItemRequest(@RequestBody ItemRequestDto requestDto,
                                                 @RequestHeader(USER_ID_HEADER) int requestorId) {
        log.info("Добавление запроса с id = {}", requestorId);
        return itemRequestService.addItemRequest(requestDto, requestorId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemRequestResponseDto> getAllRequestsByOwner(@RequestHeader(USER_ID_HEADER) int requestorId) {
        log.info("Получение запросов пользователя с id = {}", requestorId);
        return itemRequestService.getAllRequestsByOwner(requestorId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllRequests(@RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                       int from,
                                                       @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                       int size,
                                                       @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение запросов для пользователя с id = {}", userId);
        return itemRequestService.getAllRequests(from, size, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestById(@PathVariable int requestId,
                                                 @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение запроса c id = {}", requestId);
        return itemRequestService.getRequestById(requestId, userId);
    }
}