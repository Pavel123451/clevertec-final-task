package ru.clevertec.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CommentResponseDto {

    @Schema(description = "Unique identifier of the comment", example = "1")
    private Long id;

    @Schema(description = "Timestamp when the comment was created", example = "2024-11-08T15:00:00Z")
    private ZonedDateTime createdAt;

    @Schema(description = "The content of the comment", example = "This is a great article!")
    private String text;

    @Schema(description = "The username of the person who created the comment", example = "pavel1234")
    private String username;
}
