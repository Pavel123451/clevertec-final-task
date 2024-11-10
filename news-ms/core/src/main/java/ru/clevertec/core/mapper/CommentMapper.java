package ru.clevertec.core.mapper;

import org.mapstruct.*;
import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.core.model.Comment;

/**
 * Mapper interface for converting between Comment-related DTOs and entity classes.
 * This interface defines the mappings for converting comment-related data transfer objects (DTOs)
 * to domain entities and vice versa, using MapStruct.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    /**
     * Converts a CommentCreateDto to a Comment entity.
     *
     * @param commentCreateDto The DTO containing the data for creating a comment.
     * @return The Comment entity.
     */
    Comment toComment(CommentCreateDto commentCreateDto);

    /**
     * Converts a Comment entity to a CommentResponseDto.
     *
     * @param comment The Comment entity to convert.
     * @return The corresponding CommentResponseDto.
     */
    CommentResponseDto toCommentResponseDto(Comment comment);

    /**
     * Updates a Comment entity with data from a CommentCreateDto.
     *
     * @param commentCreateDto The DTO containing the updated data for the comment.
     * @param comment The Comment entity to update.
     */
    void updateComment(CommentCreateDto commentCreateDto,
                       @MappingTarget Comment comment);

    /**
     * Partially updates a Comment entity with data from a CommentPartialUpdateDto.
     * This method ignores null values in the DTO and does not update the corresponding entity properties.
     *
     * @param commentPartialUpdateDto The DTO containing the partial updates for the comment.
     * @param comment The Comment entity to partially update.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateComment(CommentPartialUpdateDto commentPartialUpdateDto,
                              @MappingTarget Comment comment);
}

