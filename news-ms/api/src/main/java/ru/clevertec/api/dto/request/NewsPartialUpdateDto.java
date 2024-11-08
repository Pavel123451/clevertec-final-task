package ru.clevertec.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NewsPartialUpdateDto {

    @Schema(description = "The updated title of the news article", example = "Updated News Title")
    private String title;

    @Schema(description = "The updated content of the news article", example = "This is the updated news content after revision.")
    private String text;
}
