package ru.clevertec.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NewsPaginatedResponseDto {

    @Schema(description = "A paginated list of news articles", implementation = PageResultDto.class)
    private PageResultDto<NewsResponseDto> newsList;
}
