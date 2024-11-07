package ru.clevertec.api.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class NewsWithCommentsResponseDto {
    private Long id;
    private ZonedDateTime createdAt;
    private String title;
    private String text;
    private PageResultDto<CommentResponseDto> comments;
}
