package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addItemRequest(ItemRequestDto requestDto, int requestorId);

    List<ItemRequestResponseDto> getAllRequestsByOwner(int requestorId);

    List<ItemRequestResponseDto> getAllRequests(int from, int size, int userId);

    ItemRequestResponseDto getRequestById(int requestId, int userId);
}