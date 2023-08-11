package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.TestUtils.ownerWithoutId;
import static ru.practicum.shareit.TestUtils.requestorWithoutId;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository requestRepository;
    private User itemOwner;
    private Item item;
    private ItemRequest request;

    @BeforeEach
    void beforeEach() {
        itemOwner = userRepository.save(ownerWithoutId);
        User user = userRepository.save(requestorWithoutId);
        request = requestRepository.save(ItemRequest.builder().requestor(user).created(LocalDateTime.now())
                .description("description").build());
        item = itemRepository.save(Item.builder().name("item").description("description")
                .available(true).itemRequest(request).owner(itemOwner).build());
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void searchTest() {
        List<Item> result = itemRepository.search("description", Pageable.unpaged());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item, result.get(0));
    }

    @Test
    void findAllTest() {
        List<Item> result = itemRepository.findAllByRequestIdIn(List.of(request));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item.getOwner(), result.get(0).getOwner());
        assertEquals(item.getName(), result.get(0).getName());
        assertEquals(item.getDescription(), result.get(0).getDescription());
    }

    @Test
    void findAllByItemRequestTest() {
        List<Item> result = itemRepository.findAllByItemRequest(request);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(item.getOwner(), result.get(0).getOwner());
        assertEquals(item.getName(), result.get(0).getName());
        assertEquals(item.getDescription(), result.get(0).getDescription());
    }
}