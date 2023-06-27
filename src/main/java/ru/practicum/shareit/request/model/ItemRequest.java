package ru.practicum.shareit.request.model;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequest {
    private int id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
