package ru.clevertec.core.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.clevertec.api.dto.request.NewsCreateDto;
import ru.clevertec.api.dto.request.NewsPartialUpdateDto;
import ru.clevertec.api.dto.response.*;
import ru.clevertec.core.model.News;

import java.util.List;

/**
 * Mapper interface for converting between News-related DTOs and entity classes.
 * This interface defines the mappings for converting news-related data transfer objects (DTOs)
 * to domain entities and vice versa, using MapStruct.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewsMapper {

    /**
     * Converts a NewsCreateDto to a News entity.
     *
     * @param newsCreateDto The DTO containing the data for creating a news article.
     * @return The News entity.
     */
    News toNews(NewsCreateDto newsCreateDto);

    /**
     * Converts a News entity to a NewsResponseDto.
     *
     * @param news The News entity to convert.
     * @return The corresponding NewsResponseDto.
     */
    NewsResponseDto toNewsResponseDto(News news);

    /**
     * Converts a News entity to a NewsWithCommentsResponseDto,
     * including a paginated list of comments.
     *
     * @param news The News entity to convert.
     * @param comments A paginated list of comment DTOs related to the news.
     * @return The NewsWithCommentsResponseDto containing the news article and its comments.
     */
    @Mapping(target = "comments", source = "comments")
    NewsWithCommentsResponseDto toNewsWithCommentsResponseDto(News news,
                                                              PageResultDto<CommentResponseDto> comments);

    /**
     * Updates a News entity with data from a NewsCreateDto.
     *
     * @param newsCreateDto The DTO containing the updated data for the news article.
     * @param news The News entity to update.
     */
    void updateNews(NewsCreateDto newsCreateDto, @MappingTarget News news);

    /**
     * Partially updates a News entity with data from a NewsPartialUpdateDto.
     * This method ignores null values in the DTO and does not update the corresponding entity properties.
     *
     * @param newsPartialUpdateDto The DTO containing the partial updates for the news article.
     * @param news The News entity to partially update.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateNews(NewsPartialUpdateDto newsPartialUpdateDto,
                           @MappingTarget News news);
}

