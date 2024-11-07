package ru.clevertec.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateDto {

    @NotBlank
    private String text;

    @NotBlank
    private String username;
}
