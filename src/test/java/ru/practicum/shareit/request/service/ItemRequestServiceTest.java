package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestUtils.requestor;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void getForOwnerTest() {
        when(userRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));
        ItemRequest request = ItemRequest.builder()
                .id(1)
                .description("request_description")
                .requestor(requestor)
                .build();
        List<ItemRequest> requests = List.of(request);
        Item item1 = Item.builder()
                .id(1).name("item_name1")
                .description("item_description1")
                .available(true)
                .itemRequest(request)
                .build();
        List<Item> items = List.of(item1);
        when(itemRequestRepository.findRequestByRequestorIdOrderByCreatedDesc(requestor.getId())).thenReturn(requests);
        when(itemRepository.findAllByRequestIdIn(requests)).thenReturn(items);

        ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .items(ItemMapper.toItemForRequestDto(items))
                .build();
        List<ItemRequestResponseDto> result = itemRequestService.getRequestForOwner(requestor.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(result.get(0), itemRequestResponseDto);
        verify(userRepository, times(1)).findById(requestor.getId());
        verify(itemRequestRepository, times(1)).findRequestByRequestorIdOrderByCreatedDesc(requestor.getId());
        verify(itemRepository, times(1)).findAllByRequestIdIn(requests);
    }

    @Test
    void findAll() {
        User requestor = new User(2, "user2", "user2@mail.com");
        User user = new User(1, "user1", "user1@mail.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ItemRequest request = ItemRequest.builder()
                .id(1)
                .description("request_description")
                .requestor(requestor)
                .build();
        Item item1 = Item.builder()
                .id(1)
                .name("item_name1")
                .description("item_description1")
                .available(true)
                .itemRequest(request)
                .build();
        List<Item> items = List.of(item1);
        List<ItemRequest> requests = List.of(request);
        Page<ItemRequest> requestsPage = new PageImpl<>(requests);
        when(itemRequestRepository.findAllForUser(eq(user.getId()), any(Pageable.class))).thenReturn(requestsPage);
        when(itemRepository.findAllByRequestIdIn(requests)).thenReturn(items);

        ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .items(ItemMapper.toItemForRequestDto(items))
                .build();
        List<ItemRequestResponseDto> result = itemRequestService.getAllRequests(0, 20, user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(result.get(0), itemRequestResponseDto);
        verify(userRepository, times(1)).findById(user.getId());
        verify(itemRequestRepository, times(1)).findAllForUser(eq(user.getId()), any(Pageable.class));
        verify(itemRepository, times(1)).findAllByRequestIdIn(requests);
    }

    @Test
    void findByIdTest() {
        User user = new User(1, "user1", "user1@mail.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ItemRequest request = ItemRequest.builder()
                .id(1)
                .description("request_description")
                .requestor(user)
                .build();
        when(itemRequestRepository.findById(request.getId())).thenReturn(Optional.of(request));

        Item item1 = Item.builder()
                .id(1)
                .name("item_name1")
                .description("item_description1")
                .available(true)
                .itemRequest(request)
                .build();
        List<Item> items = List.of(item1);
        when(itemRepository.findAllByItemRequest(request)).thenReturn(items);

        ItemRequestResponseDto itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .items(ItemMapper.toItemForRequestDto(items))
                .build();
        ItemRequestResponseDto result = itemRequestService.getRequestById(request.getId(), user.getId());

        assertNotNull(result);
        assertEquals(result, itemRequestResponseDto);
        verify(userRepository, times(1)).findById(user.getId());
        verify(itemRequestRepository, times(1)).findById(request.getId());
        verify(itemRepository, times(1)).findAllByItemRequest(request);
    }
}