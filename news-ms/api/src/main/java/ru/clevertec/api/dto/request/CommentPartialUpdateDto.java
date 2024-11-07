package ru.clevertec.api.dto.request;

import lombok.Data;

@Data
public class CommentPartialUpdateDto {
    private String text;
    private String username;
}
