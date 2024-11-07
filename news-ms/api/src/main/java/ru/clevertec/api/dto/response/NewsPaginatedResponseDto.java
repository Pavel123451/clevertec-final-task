package ru.clevertec.api.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class NewsPaginatedResponseDto {
    private PageResultDto<NewsResponseDto> newsList;
}
