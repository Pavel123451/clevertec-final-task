package ru.clevertec.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;
import ru.clevertec.core.cache.Cache;
import ru.clevertec.core.exception.CommentNotFoundException;
import ru.clevertec.core.exception.NewsNotFoundException;
import ru.clevertec.core.mapper.CommentMapper;
import ru.clevertec.core.model.Comment;
import ru.clevertec.core.model.News;
import ru.clevertec.core.repository.CommentRepository;
import ru.clevertec.core.repository.NewsRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;
    private final Cache<Long, Comment> cache;

    public CommentResponseDto createComment(Long newsId,
                                            CommentCreateDto commentCreateDto
    ) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException("News with id " + newsId + " not found"));

        Comment comment = commentMapper.toComment(commentCreateDto);
        comment.setNews(news);

        Comment savedComment = commentRepository.save(comment);
        cache.put(savedComment.getId(), savedComment);

        return commentMapper.toCommentResponseDto(savedComment);
    }

    public CommentResponseDto getComment(Long newsId, Long commentId) {
        Comment comment = cache.get(commentId);
        if (comment == null) {
            comment = commentRepository.findByNewsIdAndId(newsId, commentId)
                    .orElseThrow(() -> new CommentNotFoundException(
                            "News with id " + newsId + " has no comment with id " + commentId));
            cache.put(commentId, comment);
        }

        return commentMapper.toCommentResponseDto(comment);
    }

    public CommentResponseDto updateComment(Long newsId, Long commentId,
                                            CommentCreateDto commentCreateDto
    ) {
        Comment comment = getCommentFromCacheOrRepository(newsId, commentId);

        commentMapper.updateComment(commentCreateDto, comment);
        Comment updatedComment = commentRepository.save(comment);
        cache.put(commentId, updatedComment);

        return commentMapper.toCommentResponseDto(updatedComment);
    }

    public CommentResponseDto partialUpdateComment(Long newsId,
                                                   Long commentId,
                                                   CommentPartialUpdateDto commentPartialUpdateDto
    ) {
        Comment comment = getCommentFromCacheOrRepository(newsId, commentId);

        commentMapper.partialUpdateComment(commentPartialUpdateDto, comment);
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toCommentResponseDto(updatedComment);
    }

    public String deleteComment(Long newsId, Long commentId) {
        Comment comment = commentRepository.findByNewsIdAndId(newsId, commentId)
                .orElseThrow(() -> new CommentNotFoundException(
                        "News with id " + newsId + " has no comment with id " + commentId));

        commentRepository.delete(comment);
        cache.delete(commentId);

        return "Comment with ID " + commentId + " has been deleted.";
    }

    public PageResultDto<CommentResponseDto> searchComments(Long newsId,
                                                            String text,
                                                            int page, int size
    ) {
        if (!newsRepository.existsById(newsId)) {
            throw new NewsNotFoundException("News with id " + newsId + " not found");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository
                .findByTextContainsIgnoreCaseAndNewsId(text, pageable, newsId);

        return new PageResultDto<>(commentPage.map(commentMapper::toCommentResponseDto));
    }

    private Comment getCommentFromCacheOrRepository(Long newsId, Long commentId) {
        Comment comment = cache.get(commentId);
        if (comment == null) {
            comment = commentRepository.findByNewsIdAndId(newsId, commentId)
                    .orElseThrow(() -> new CommentNotFoundException(
                            "News with id " + newsId + " has no comment with id " + commentId));
        }
        return comment;
    }
}


