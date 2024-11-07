package ru.clevertec.core.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.clevertec.api.dto.request.NewsCreateDto;
import ru.clevertec.api.dto.request.NewsPartialUpdateDto;
import ru.clevertec.api.dto.response.*;
import ru.clevertec.core.model.News;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    News toNews(NewsCreateDto newsCreateDto);

    NewsResponseDto toNewsResponseDto(News news);


    @Mapping(target = "comments", source = "comments")
    NewsWithCommentsResponseDto toNewsWithCommentsResponseDto(News news,
                                                              PageResultDto<CommentResponseDto> comments);


    void updateNews(NewsCreateDto newsCreateDto, @MappingTarget News news);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateNews(NewsPartialUpdateDto newsPartialUpdateDto,
                                      @MappingTarget News news);
}
