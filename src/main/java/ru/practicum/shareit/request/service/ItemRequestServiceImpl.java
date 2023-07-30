package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.permanentunits.ShareitPageRequest;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.permanentunits.Sorts.SORT_BY_CREATED_DESC;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestResponseDto addItemRequest(ItemRequestDto requestDto, int requestorId) {
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto);
        User user = findUser(requestorId);
        request.setRequestor(user);
        return ItemRequestMapper.toResponseItemRequestDto(itemRequestRepository.save(request));
    }

    @Override
    public List<ItemRequestResponseDto> getRequestForOwner(int requestorId) {
        findUser(requestorId);
        List<ItemRequest> requests = itemRequestRepository.findRequestByRequestorIdOrderByCreatedDesc(requestorId);
        Map<ItemRequest, List<Item>> itemsByRequest = findItemsByRequests(requests);
        return requests.stream()
                .map(request -> ItemRequestMapper.toResponseItemRequestDto(request, itemsByRequest.get(request)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponseDto> getAllRequests(int from, int size, int userId) {
        findUser(userId);
        Pageable page = new ShareitPageRequest(from, size, SORT_BY_CREATED_DESC);
        List<ItemRequest> requests = itemRequestRepository.findAllForUser(userId, page).toList();
        Map<ItemRequest, List<Item>> itemsByRequest = findItemsByRequests(requests);
        return itemsByRequest.entrySet().stream()
                .map(entry -> ItemRequestMapper.toResponseItemRequestDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestResponseDto getRequestById(int requestId, int userId) {
        findUser(userId);
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND, "Ресурс не найден"));
        List<Item> items = itemRepository.findAllByItemRequest(request);
        return ItemRequestMapper.toResponseItemRequestDto(request, items);
    }

    private Map<ItemRequest, List<Item>> findItemsByRequests(List<ItemRequest> requests) {
        return itemRepository.findAllByRequestIdIn(requests)
                .stream()
                .collect(Collectors.groupingBy(Item::getItemRequest, Collectors.toList()));
    }

    private User findUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ValidationException(HttpStatus.NOT_FOUND, "Ресурс не найден"));
    }
}