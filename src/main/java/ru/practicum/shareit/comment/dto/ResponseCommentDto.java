package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCommentDto {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
