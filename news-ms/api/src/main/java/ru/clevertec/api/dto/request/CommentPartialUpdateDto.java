package ru.clevertec.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentPartialUpdateDto {

    @Schema(description = "The updated content of the comment", example = "Updated comment text.")
    private String text;

    @Schema(description = "The updated username of the commenter", example = "pavel124")
    private String username;
}
