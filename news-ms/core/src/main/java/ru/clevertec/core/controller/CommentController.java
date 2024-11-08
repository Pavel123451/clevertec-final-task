package ru.clevertec.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.api.CommentApi;
import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;
import ru.clevertec.core.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentApi {

    private final CommentService commentService;

    @Override
    public CommentResponseDto createComment(Long newsId,
                                            CommentCreateDto commentCreateDto
    ) {
        return commentService.createComment(newsId, commentCreateDto);
    }

    @Override
    public CommentResponseDto getComment(Long newsId, Long commentId) {
        return commentService.getComment(newsId, commentId);
    }

    @Override
    public CommentResponseDto updateComment(Long newsId,
                                            Long commentId,
                                            CommentCreateDto commentCreateDto
    ) {
        return commentService.updateComment(newsId, commentId, commentCreateDto);
    }

    @Override
    public CommentResponseDto partialUpdateComment(Long newsId,
                                                   Long commentId,
                                                   CommentPartialUpdateDto commentPartialUpdateDto
    ) {
        return commentService.partialUpdateComment(newsId, commentId, commentPartialUpdateDto);
    }

    @Override
    public String deleteComment(Long newsId, Long commentId) {
        return commentService.deleteComment(newsId, commentId);
    }

    @Override
    public PageResultDto<CommentResponseDto> searchComments(Long newsId,
                                                            String text,
                                                            int page,
                                                            int size
    ) {
        return commentService.searchComments(newsId, text, page, size);
    }
}
