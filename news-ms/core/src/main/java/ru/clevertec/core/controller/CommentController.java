package ru.clevertec.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.api.CommentApi;
import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;
import ru.clevertec.core.service.CommentService;

/**
 * REST controller for handling requests related to comments on news articles.
 * This controller provides endpoints for creating, retrieving, updating,
 * deleting, and searching comments associated with specific news articles.
 */
@RestController
@RequiredArgsConstructor
public class CommentController implements CommentApi {

    private final CommentService commentService;

    /**
     * Creates a new comment for a specific news article.
     *
     * @param newsId The ID of the news article for which the comment is being created.
     * @param commentCreateDto The DTO containing the data for the new comment.
     * @return The response DTO containing the created comment's details.
     */
    @Override
    public CommentResponseDto createComment(Long newsId,
                                            CommentCreateDto commentCreateDto) {
        return commentService.createComment(newsId, commentCreateDto);
    }

    /**
     * Retrieves a comment by its ID for a specific news article.
     *
     * @param newsId The ID of the news article.
     * @param commentId The ID of the comment.
     * @return The response DTO containing the comment's details.
     */
    @Override
    public CommentResponseDto getComment(Long newsId, Long commentId) {
        return commentService.getComment(newsId, commentId);
    }

    /**
     * Updates an existing comment for a specific news article.
     *
     * @param newsId The ID of the news article.
     * @param commentId The ID of the comment.
     * @param commentCreateDto The DTO containing the updated data for the comment.
     * @return The response DTO containing the updated comment's details.
     */
    @Override
    public CommentResponseDto updateComment(Long newsId,
                                            Long commentId,
                                            CommentCreateDto commentCreateDto) {
        return commentService.updateComment(newsId, commentId, commentCreateDto);
    }

    /**
     * Partially updates an existing comment for a specific news article.
     *
     * @param newsId The ID of the news article.
     * @param commentId The ID of the comment.
     * @param commentPartialUpdateDto The DTO containing the updated fields of the comment.
     * @return The response DTO containing the updated comment's details.
     */
    @Override
    public CommentResponseDto partialUpdateComment(Long newsId,
                                                   Long commentId,
                                                   CommentPartialUpdateDto commentPartialUpdateDto) {
        return commentService.partialUpdateComment(newsId, commentId, commentPartialUpdateDto);
    }

    /**
     * Deletes a comment by its ID for a specific news article.
     *
     * @param newsId The ID of the news article.
     * @param commentId The ID of the comment.
     * @return A message indicating the deletion result.
     */
    @Override
    public String deleteComment(Long newsId, Long commentId) {
        return commentService.deleteComment(newsId, commentId);
    }

    /**
     * Searches for comments by a keyword in their text for a specific news article.
     *
     * @param newsId The ID of the news article.
     * @param text The text to search for within comments.
     * @param page The page number to retrieve.
     * @param size The number of comments per page.
     * @return A paginated result containing matching comments.
     */
    @Override
    public PageResultDto<CommentResponseDto> searchComments(Long newsId,
                                                            String text,
                                                            int page,
                                                            int size) {
        return commentService.searchComments(newsId, text, page, size);
    }
}

