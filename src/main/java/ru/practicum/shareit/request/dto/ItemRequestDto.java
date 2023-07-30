package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.validation.marker.Create;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @NotBlank(groups = {Create.class}, message = "Description у Request не может быть пустым")
    private String description;
}
