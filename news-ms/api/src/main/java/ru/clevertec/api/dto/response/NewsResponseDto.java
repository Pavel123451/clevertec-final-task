package ru.clevertec.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class NewsResponseDto {

    @Schema(description = "Unique identifier of the news article", example = "100")
    private Long id;

    @Schema(description = "Timestamp when the news article was created", example = "2024-11-08T14:00:00Z")
    private ZonedDateTime createdAt;

    @Schema(description = "The title of the news article", example = "Breaking News: Major Event Happens")
    private String title;

    @Schema(description = "The content of the news article", example = "Details about the major event that took place today.")
    private String text;
}
