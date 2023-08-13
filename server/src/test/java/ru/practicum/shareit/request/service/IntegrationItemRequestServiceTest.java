package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationItemRequestServiceTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Test
    void getForOwner() {
        UserDto owner = new UserDto(1, "owner", "owner@mail.com");
        UserDto requestor = new UserDto(2, "requestor", "requestor@mail.com");

        UserDto createdOwner = userService.addUser(owner);
        UserDto createdRequestor = userService.addUser(requestor);
        itemService.addItem(ItemDto.builder()
                        .name("item1")
                        .description("description1")
                        .available(true).build(),
                createdOwner.getId()
        );
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("request_descritpion")
                .build();
        ItemRequestResponseDto request = itemRequestService.addItemRequest(itemRequestDto, createdRequestor.getId());

        ItemRequestResponseDto gottenItemRequest = itemRequestService.getRequestById(request.getId(), createdRequestor.getId());
        TypedQuery<ItemRequest> query = em.createQuery("SELECT i FROM requests AS i WHERE i.id = :requestId", ItemRequest.class);
        ItemRequest gottenItemRequestFromDB = query
                .setParameter("requestId", request.getId())
                .getSingleResult();

        assertThat(gottenItemRequest.getId(), equalTo(gottenItemRequestFromDB.getId()));
        assertThat(gottenItemRequest.getCreated(), equalTo(gottenItemRequestFromDB.getCreated()));

        userService.deleteUserById(createdOwner.getId());
        userService.deleteUserById(createdRequestor.getId());
    }
}