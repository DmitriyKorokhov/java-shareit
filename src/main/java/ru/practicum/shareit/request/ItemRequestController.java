package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.marker.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_FROM_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.DEFAULT_SIZE_VALUE;
import static ru.practicum.shareit.permanentunits.Constants.USER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto addRequest(@Validated({Create.class}) @RequestBody ItemRequestDto requestDto,
                                             @RequestHeader(USER_ID_HEADER) int requestorId) {
        log.info("Добавление запроса с id = {}", requestorId);
        return itemRequestService.addItemRequest(requestDto, requestorId);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getAllRequestsByOwner(@RequestHeader(USER_ID_HEADER) int requestorId) {
        log.info("Получение запросов пользователя с id = {}", requestorId);
        return itemRequestService.getRequestForOwner(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllRequests(@RequestParam(defaultValue = DEFAULT_FROM_VALUE)
                                                       @PositiveOrZero int from,
                                                       @RequestParam(defaultValue = DEFAULT_SIZE_VALUE)
                                                       @Positive int size,
                                                       @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение запросов для пользователя с id = {}", userId);
        return itemRequestService.getAllRequests(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestById(@PathVariable int requestId,
                                                 @RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получение запроса c id = {}", requestId);
        return itemRequestService.getRequestById(requestId, userId);
    }
}