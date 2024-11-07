package ru.clevertec.core.mapper;

import org.mapstruct.*;
import org.springframework.data.domain.Page;
import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.core.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    Comment toComment(CommentCreateDto commentCreateDto);

    CommentResponseDto toCommentResponseDto(Comment comment);

    void updateComment(CommentCreateDto commentCreateDto,
                       @MappingTarget Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateComment(CommentPartialUpdateDto commentPartialUpdateDto,
                              @MappingTarget Comment comment);


}
