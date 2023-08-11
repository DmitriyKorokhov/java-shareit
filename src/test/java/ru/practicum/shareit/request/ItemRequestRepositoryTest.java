package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private User requestor;
    private ItemRequest request;

    @BeforeEach
    void beforeEach() {
        LocalDateTime dateTime = LocalDateTime.now();
        user = userRepository.save(User.builder()
                .name("user")
                .email("user@mail.com")
                .build());
        requestor = userRepository.save(User.builder()
                .name("requestor")
                .email("requestor@mail.com")
                .build());
        request = itemRequestRepository.save(ItemRequest.builder()
                .requestor(requestor)
                .description("description")
                .created(dateTime)
                .build());
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    public void findRequestByRequestorIdOrderByCreatedDescTest() {
        List<ItemRequest> result = itemRequestRepository
                .findRequestByRequestorIdOrderByCreatedDesc(requestor.getId());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(request.getDescription(), result.get(0).getDescription());
        assertEquals(request.getRequestor(), result.get(0).getRequestor());
        assertEquals(request.getCreated(), result.get(0).getCreated());
    }

    @Test
    public void findAllForUserTest() {
        Page<ItemRequest> result = itemRequestRepository.findAllForUser(user.getId(), Pageable.unpaged());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(request.getDescription(), result.getContent().get(0).getDescription());
        assertEquals(request.getRequestor(), result.getContent().get(0).getRequestor());
        assertEquals(request.getCreated(), result.getContent().get(0).getCreated());
    }
}