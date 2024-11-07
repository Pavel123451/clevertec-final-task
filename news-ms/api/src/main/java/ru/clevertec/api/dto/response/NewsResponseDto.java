package ru.clevertec.api.dto.response;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class NewsResponseDto {
    private Long id;
    private ZonedDateTime createdAt;
    private String title;
    private String text;
}
