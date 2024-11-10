package ru.clevertec.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;
import ru.clevertec.core.cache.Cache;
import ru.clevertec.core.mapper.CommentMapper;
import ru.clevertec.core.model.Comment;
import ru.clevertec.core.model.News;
import ru.clevertec.core.repository.CommentRepository;
import ru.clevertec.core.repository.NewsRepository;
import ru.clevertec.exceptionhandlestarter.exception.CommentNotFoundException;
import ru.clevertec.exceptionhandlestarter.exception.NewsNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;
    private final Cache<Long, Comment> cache;

    public CommentResponseDto createComment(Long newsId,
                                            CommentCreateDto commentCreateDto) {
        log.debug("Creating comment for newsId: {} with data: {}",
                newsId, commentCreateDto);

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException(
                        "News with id " + newsId + " not found"));

        Comment comment = commentMapper.toComment(commentCreateDto);
        comment.setNews(news);

        Comment savedComment = commentRepository.save(comment);
        cache.put(savedComment.getId(), savedComment);

        log.debug("Comment created with id: {}", savedComment.getId());
        return commentMapper.toCommentResponseDto(savedComment);
    }

    public CommentResponseDto getComment(Long newsId, Long commentId) {
        log.debug("Fetching comment with id: {} for newsId: {}", commentId, newsId);

        Comment comment = cache.get(commentId);
        if (comment == null) {
            comment = commentRepository.findByNewsIdAndId(newsId, commentId)
                    .orElseThrow(() -> new CommentNotFoundException(
                            "News with id " + newsId + " has no comment with id " + commentId));
            cache.put(commentId, comment);
        }

        log.debug("Comment found with id: {}", commentId);
        return commentMapper.toCommentResponseDto(comment);
    }

    public CommentResponseDto updateComment(Long newsId,
                                            Long commentId,
                                            CommentCreateDto commentCreateDto) {
        log.debug("Updating comment with id: {} for newsId: {} with data: {}",
                commentId, newsId, commentCreateDto);

        Comment comment = getCommentFromCacheOrRepository(newsId, commentId);

        commentMapper.updateComment(commentCreateDto, comment);
        Comment updatedComment = commentRepository.save(comment);
        cache.put(commentId, updatedComment);

        log.debug("Comment updated with id: {}", commentId);
        return commentMapper.toCommentResponseDto(updatedComment);
    }

    public CommentResponseDto partialUpdateComment(Long newsId,
                                                   Long commentId,
                                                   CommentPartialUpdateDto commentPartialUpdateDto) {
        log.debug("Partially updating comment with id: {} for newsId: {}",
                commentId, newsId);

        Comment comment = getCommentFromCacheOrRepository(newsId, commentId);

        commentMapper.partialUpdateComment(commentPartialUpdateDto, comment);
        Comment updatedComment = commentRepository.save(comment);

        log.debug("Comment partially updated with ID: {}", commentId);
        return commentMapper.toCommentResponseDto(updatedComment);
    }

    public String deleteComment(Long newsId, Long commentId) {
        log.debug("Deleting comment with id: {} for newsId: {}", commentId, newsId);

        Comment comment = commentRepository.findByNewsIdAndId(newsId, commentId)
                .orElseThrow(() -> new CommentNotFoundException(
                        "News with id " + newsId + " has no comment with id " + commentId));

        commentRepository.delete(comment);
        cache.delete(commentId);

        log.debug("Comment deleted with ID: {}", commentId);
        return "Comment with id " + commentId + " has been deleted.";
    }

    public PageResultDto<CommentResponseDto> searchComments(Long newsId,
                                                            String text,
                                                            int page, int size) {
        log.debug("Searching comments for newsId: {} with text: '{}' on page: {} with size: {}",
                newsId, text, page, size);

        if (!newsRepository.existsById(newsId)) {
            throw new NewsNotFoundException("News with id " + newsId + " not found");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository
                .findByTextContainsIgnoreCaseAndNewsId(text, pageable, newsId);

        log.debug("Found {} comments for search", commentPage.getTotalElements());
        return new PageResultDto<>(commentPage.map(commentMapper::toCommentResponseDto));
    }

    private Comment getCommentFromCacheOrRepository(Long newsId, Long commentId) {
        Comment comment = cache.get(commentId);
        if (comment == null) {
            log.debug("Cache miss for commentId: {}, fetching from repository", commentId);
            comment = commentRepository.findByNewsIdAndId(newsId, commentId)
                    .orElseThrow(() -> new CommentNotFoundException(
                            "News with id " + newsId + " has no comment with id " + commentId));
        }
        return comment;
    }
}


