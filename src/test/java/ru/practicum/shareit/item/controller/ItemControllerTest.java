package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestUtils.item;
import static ru.practicum.shareit.permanentunits.Constants.USER_ID_HEADER;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private ItemDto itemDto;
    private ItemResponseDto itemResponseDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getItemRequest().getId())
                .build();
        itemResponseDto = ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getItemRequest().getId())
                .build();
    }

    @Test
    void createItemTest() throws Exception {
        when(itemService.addItem(any(), any(Integer.class))).thenReturn(itemDto);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())));
    }

    @Test
    void createCommentTest() throws Exception {
        Comment comment = Comment.builder()
                .id(1)
                .text("text")
                .created(LocalDateTime.now())
                .build();
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
        when(itemService.addCommentForItem(any(), any(Integer.class), any(Integer.class))).thenReturn(commentResponseDto);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Integer.class))
                .andExpect(jsonPath("$.created", is(notNullValue())))
                .andExpect(jsonPath("$.text", is(comment.getText())));
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(any(), any(Integer.class))).thenReturn(itemDto);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())));
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemForUser(any(Integer.class), any(Integer.class))).thenReturn(itemResponseDto);
        mvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())));
    }

    @Test
    void getAllItemsOwnerTest() throws Exception {
        when(itemService.getUsersAllItems(any(Integer.class), eq(0), eq(20))).thenReturn(List.of(itemResponseDto));
        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(itemDto.getId())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(itemDto.getName())))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(itemDto.getDescription())))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(itemDto.getAvailable())))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(itemDto.getRequestId())));
    }

    @Test
    void getAllItemsOwnerWithPagination() throws Exception {
        when(itemService.getUsersAllItems(any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(List.of(itemResponseDto));
        mvc.perform(get("/items?from=0&size=20")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(itemDto.getId())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(itemDto.getName())))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(itemDto.getDescription())))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(itemDto.getAvailable())))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(itemDto.getRequestId())));
    }

    @Test
    void searchItemByTextTest() throws Exception {
        when(itemService.findItemsByText(any(), eq(0), eq(20))).thenReturn(List.of(itemResponseDto));
        mvc.perform(get("/items/search?text=text")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(itemDto.getId())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(itemDto.getName())))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(itemDto.getDescription())))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(itemDto.getAvailable())))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(itemDto.getRequestId())));
    }

    @Test
    void searchItemByTextTestWithPagination() throws Exception {
        when(itemService.findItemsByText(any(), any(Integer.class), any(Integer.class))).thenReturn(List.of(itemResponseDto));
        mvc.perform(get("/items/search?text=text&from=0&size=20")
                        .header(USER_ID_HEADER, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(itemDto.getId())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(itemDto.getName())))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(itemDto.getDescription())))
                .andExpect(jsonPath("$[*].available", containsInAnyOrder(itemDto.getAvailable())))
                .andExpect(jsonPath("$[*].requestId", containsInAnyOrder(itemDto.getRequestId())));
    }
}