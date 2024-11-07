package ru.clevertec.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewsCreateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String text;
}
