package ru.clevertec.api.dto.request;

import lombok.Data;

@Data
public class NewsPartialUpdateDto {
    private String title;
    private String text;
}
