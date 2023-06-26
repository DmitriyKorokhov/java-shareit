package ru.practicum.shareit.user.model;

import lombok.*;

@Data
@Builder
public class User {
    private Integer id;
    private String name;
    private String email;
}
