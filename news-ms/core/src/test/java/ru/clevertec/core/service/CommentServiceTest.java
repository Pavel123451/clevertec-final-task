package ru.clevertec.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.core.data.CommentTestData.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private static final Long NEWS_ID = 1L;
    private static final Long COMMENT_ID = 1L;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private Cache<Long, Comment> cache;
    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_shouldReturnCommentResponseDto() {
        CommentCreateDto commentCreateDto = COMMENT_CREATE_DTO;
        News news = new News();
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setText(commentCreateDto.getText());
        comment.setUsername(commentCreateDto.getUsername());
        comment.setNews(news);

        Mockito.when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(news));
        Mockito.when(commentMapper.toComment(commentCreateDto)).thenReturn(comment);
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Mockito.when(commentMapper.toCommentResponseDto(comment)).thenReturn(COMMENT_RESPONSE_DTO);

        CommentResponseDto result = commentService.createComment(NEWS_ID, commentCreateDto);

        assertNotNull(result);
        assertEquals(COMMENT_RESPONSE_DTO.getId(), result.getId());
        assertEquals(COMMENT_RESPONSE_DTO.getText(), result.getText());

        verify(newsRepository, times(1)).findById(NEWS_ID);
        verify(commentRepository, times(1)).save(comment);
        verify(cache, times(1)).put(COMMENT_ID, comment);
        verify(commentMapper, times(1)).toCommentResponseDto(comment);
    }

    @Test
    void getComment_shouldReturnCommentResponseDtoFromCache() {
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setText("Existing comment");
        Mockito.when(cache.get(COMMENT_ID)).thenReturn(comment);
        Mockito.when(commentMapper.toCommentResponseDto(comment)).thenReturn(COMMENT_RESPONSE_DTO);

        CommentResponseDto result = commentService.getComment(NEWS_ID, COMMENT_ID);

        assertNotNull(result);
        assertEquals(COMMENT_RESPONSE_DTO.getId(), result.getId());

        verify(cache, times(1)).get(COMMENT_ID);
        verify(commentMapper, times(1)).toCommentResponseDto(comment);
    }

    @Test
    void getComment_shouldFetchCommentFromRepositoryWhenNotInCache() {
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setText("Existing comment");

        Mockito.when(cache.get(COMMENT_ID)).thenReturn(null); // Cache miss
        Mockito.when(commentRepository.findByNewsIdAndId(NEWS_ID, COMMENT_ID))
                .thenReturn(Optional.of(comment));
        Mockito.when(commentMapper.toCommentResponseDto(comment)).thenReturn(COMMENT_RESPONSE_DTO);

        CommentResponseDto result = commentService.getComment(NEWS_ID, COMMENT_ID);

        assertNotNull(result);
        assertEquals(COMMENT_RESPONSE_DTO.getId(), result.getId());

        verify(cache,times(1)).get(COMMENT_ID);
        verify(commentRepository,times(1)).findByNewsIdAndId(NEWS_ID, COMMENT_ID);
        verify(cache,times(1)).put(COMMENT_ID, comment);
        verify(commentMapper,times(1)).toCommentResponseDto(comment);
    }

    @Test
    void getComment_shouldThrowExceptionWhenCommentNotFound() {
        Mockito.when(commentRepository.findByNewsIdAndId(NEWS_ID, COMMENT_ID))
                .thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () ->
                commentService.getComment(NEWS_ID, COMMENT_ID)
        );

        verify(commentRepository,times(1)).findByNewsIdAndId(NEWS_ID, COMMENT_ID);
    }

    @Test
    void updateComment_shouldReturnUpdatedCommentResponseDto() {
        CommentCreateDto commentCreateDto = COMMENT_CREATE_DTO;
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setText("Original text");

        Mockito.when(commentRepository.findByNewsIdAndId(NEWS_ID, COMMENT_ID))
                .thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Mockito.when(commentMapper.toCommentResponseDto(comment)).thenReturn(COMMENT_RESPONSE_DTO);

        CommentResponseDto result = commentService.updateComment(NEWS_ID, COMMENT_ID, commentCreateDto);

        assertNotNull(result);
        assertEquals(COMMENT_RESPONSE_DTO.getId(), result.getId());
        assertEquals(commentCreateDto.getText(), result.getText());

        verify(commentRepository,times(1)).findByNewsIdAndId(NEWS_ID, COMMENT_ID);
        verify(commentRepository,times(1)).save(comment);
        verify(cache,times(1)).put(COMMENT_ID, comment);
        verify(commentMapper,times(1)).toCommentResponseDto(comment);
    }

    @Test
    void partialUpdateComment_shouldReturnUpdatedCommentResponseDto() {
        CommentPartialUpdateDto partialUpdateDto = COMMENT_PARTIAL_UPDATE_DTO;
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);
        comment.setText("Original text");

        Mockito.when(commentRepository.findByNewsIdAndId(NEWS_ID, COMMENT_ID))
                .thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);
        Mockito.when(commentMapper.toCommentResponseDto(comment)).thenReturn(UPDATED_COMMENT_RESPONSE_DTO);

        CommentResponseDto result = commentService.partialUpdateComment(NEWS_ID, COMMENT_ID, partialUpdateDto);

        assertNotNull(result);
        assertEquals(UPDATED_COMMENT_RESPONSE_DTO.getId(), result.getId());
        assertEquals(partialUpdateDto.getText(), result.getText());

        verify(commentRepository,times(1)).findByNewsIdAndId(NEWS_ID, COMMENT_ID);
        verify(commentRepository,times(1)).save(comment);
        verify(commentMapper,times(1)).toCommentResponseDto(comment);
    }

    @Test
    void deleteComment_shouldReturnSuccessMessage() {
        Comment comment = new Comment();
        comment.setId(COMMENT_ID);

        Mockito.when(commentRepository.findByNewsIdAndId(NEWS_ID, COMMENT_ID))
                .thenReturn(Optional.of(comment));
        Mockito.doNothing().when(commentRepository).delete(comment);
        Mockito.doNothing().when(cache).delete(COMMENT_ID);

        String result = commentService.deleteComment(NEWS_ID, COMMENT_ID);

        assertNotNull(result);
        assertEquals("Comment with id " + COMMENT_ID + " has been deleted.", result);

        verify(commentRepository,times(1)).findByNewsIdAndId(NEWS_ID, COMMENT_ID);
        verify(commentRepository,times(1)).delete(comment);
        verify(cache,times(1)).delete(COMMENT_ID);
    }

    @Test
    void deleteComment_shouldThrowExceptionWhenCommentNotFound() {
        Mockito.when(commentRepository.findByNewsIdAndId(NEWS_ID, COMMENT_ID))
                .thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () ->
                commentService.deleteComment(NEWS_ID, COMMENT_ID)
        );

        verify(commentRepository,times(1)).findByNewsIdAndId(NEWS_ID, COMMENT_ID);
    }

    @Test
    void searchComments_shouldReturnPageOfComments() {
        String searchText = "test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(List.of(new Comment()));

        Mockito.when(newsRepository.existsById(NEWS_ID)).thenReturn(true);
        Mockito.when(commentRepository.findByTextContainsIgnoreCaseAndNewsId(searchText, pageable, NEWS_ID))
                .thenReturn(commentPage);

        PageResultDto<CommentResponseDto> result = commentService.searchComments(NEWS_ID, searchText, 0, 10);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());

        verify(newsRepository,times(1)).existsById(NEWS_ID);
        verify(commentRepository,times(1)).findByTextContainsIgnoreCaseAndNewsId(searchText, pageable, NEWS_ID);
    }

    @Test
    void searchComments_shouldThrowExceptionWhenNewsNotFound() {
        String searchText = "test";

        Mockito.when(newsRepository.existsById(NEWS_ID)).thenReturn(false);

        assertThrows(NewsNotFoundException.class, () ->
                commentService.searchComments(NEWS_ID, searchText, 0, 10)
        );

        verify(newsRepository,times(1)).existsById(NEWS_ID);
    }
}


