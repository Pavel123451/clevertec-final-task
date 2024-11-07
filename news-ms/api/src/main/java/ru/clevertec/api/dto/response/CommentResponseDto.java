package ru.clevertec.api.dto.response;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private ZonedDateTime createdAt;
    private String text;
    private String username;
}
