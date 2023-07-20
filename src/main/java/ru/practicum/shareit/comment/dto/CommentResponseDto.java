package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponseDto {
    private int id;
    @NotBlank(message = "Text у Comment е должен быть пустым")
    private String text;
    @NotBlank(message = "AuthorName у Comment е должен быть пустым")
    private String authorName;
    @NotNull(message = "Время created у Comment е должен быть пустым")
    private LocalDateTime created;
}