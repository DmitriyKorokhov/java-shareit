package ru.practicum.shareit.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @NotBlank(message = "Text не должен быть пустым")
    private String text;
}