package ru.clevertec.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreateDto {

    @NotBlank
    @Schema(description = "The content of the comment", example = "This is a great article!")
    private String text;

    @NotBlank
    @Schema(description = "The username of the person creating the comment", example = "pavel1234")
    private String username;
}
