package ru.clevertec.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewsCreateDto {

    @NotBlank
    @Schema(description = "The title of the news article", example = "Breaking News: Major Event Happens")
    private String title;

    @NotBlank
    @Schema(description = "The content of the news article", example = "Details about the major event that took place today.")
    private String text;
}
