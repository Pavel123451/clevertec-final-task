package ru.clevertec.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.api.dto.response.NewsResponseDto;
import ru.clevertec.api.dto.response.NewsWithCommentsResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;
import ru.clevertec.core.cache.Cache;
import ru.clevertec.core.mapper.CommentMapper;
import ru.clevertec.core.mapper.NewsMapper;
import ru.clevertec.core.model.Comment;
import ru.clevertec.core.model.News;
import ru.clevertec.core.repository.CommentRepository;
import ru.clevertec.core.repository.NewsRepository;
import ru.clevertec.exceptionhandlestarter.exception.NewsNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.clevertec.core.data.NewsTestData.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    private static final Long NEWS_ID = 1L;

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsMapper newsMapper;
    @Mock
    private Cache<Long, News> cache;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private NewsService newsService;


    @Test
    void getNewsPage_shouldReturnPageOfNews() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> newsPage = new PageImpl<>(List.of(new News()));
        when(newsRepository.findAll(pageable)).thenReturn(newsPage);
        when(newsMapper.toNewsResponseDto(any(News.class))).thenReturn(NEWS_RESPONSE_DTO);

        PageResultDto<NewsResponseDto> result = newsService.getNewsPage(0, 10);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        assertEquals(NEWS_RESPONSE_DTO.getId(), result.getContent().get(0).getId());

        verify(newsRepository, times(1)).findAll(pageable);
        verify(newsMapper, times(1)).toNewsResponseDto(any(News.class));
    }

    @Test
    void getNewsWithComments_shouldReturnNewsWithComments() {
        Pageable pageable = PageRequest.of(0, 10);
        News news = new News();
        Page<Comment> commentPage = new PageImpl<>(List.of(new Comment()));
        PageResultDto<CommentResponseDto> commentResponseDtoPage = new PageResultDto<>(commentPage.map(commentMapper::toCommentResponseDto));

        when(cache.get(NEWS_ID)).thenReturn(null);
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(news));
        when(commentRepository.findByNewsId(NEWS_ID, pageable)).thenReturn(commentPage);

        NewsWithCommentsResponseDto newsWithCommentsResponseDto = new NewsWithCommentsResponseDto();
        newsWithCommentsResponseDto.setId(NEWS_RESPONSE_DTO.getId());
        newsWithCommentsResponseDto.setCreatedAt(NEWS_RESPONSE_DTO.getCreatedAt());
        newsWithCommentsResponseDto.setTitle(NEWS_RESPONSE_DTO.getTitle());
        newsWithCommentsResponseDto.setText(NEWS_RESPONSE_DTO.getText());
        newsWithCommentsResponseDto.setComments(commentResponseDtoPage);

        when(newsMapper.toNewsWithCommentsResponseDto(news, commentResponseDtoPage)).thenReturn(newsWithCommentsResponseDto);

        NewsWithCommentsResponseDto result = newsService.getNewsWithComments(0, 10, NEWS_ID);

        assertNotNull(result);
        assertEquals(NEWS_RESPONSE_DTO.getId(), result.getId());

        verify(cache, times(1)).get(NEWS_ID);
        verify(newsRepository, times(1)).findById(NEWS_ID);
        verify(commentRepository, times(1)).findByNewsId(NEWS_ID, pageable);
        verify(newsMapper, times(1)).toNewsWithCommentsResponseDto(news, commentResponseDtoPage);
    }



    @Test
    void createNews_shouldReturnCreatedNews() {
        News news = new News();
        when(newsMapper.toNews(NEWS_CREATE_DTO)).thenReturn(news);
        when(newsRepository.save(news)).thenReturn(news);
        when(newsMapper.toNewsResponseDto(news)).thenReturn(NEWS_RESPONSE_DTO);

        NewsResponseDto result = newsService.createNews(NEWS_CREATE_DTO);

        assertNotNull(result);
        assertEquals(NEWS_RESPONSE_DTO.getId(), result.getId());

        verify(newsRepository, times(1)).save(news);
        verify(cache, times(1)).put(news.getId(), news);
        verify(newsMapper, times(1)).toNewsResponseDto(news);
    }

    @Test
    void updateNews_shouldReturnUpdatedNews() {
        News news = new News();
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(news));
        when(newsRepository.save(news)).thenReturn(news);
        when(newsMapper.toNewsResponseDto(news)).thenReturn(UPDATED_NEWS_RESPONSE_DTO);

        NewsResponseDto result = newsService.updateNews(NEWS_ID, NEWS_CREATE_DTO);

        assertNotNull(result);
        assertEquals(UPDATED_NEWS_RESPONSE_DTO.getId(), result.getId());
        assertEquals(UPDATED_NEWS_RESPONSE_DTO.getTitle(), result.getTitle());

        verify(newsRepository, times(1)).findById(NEWS_ID);
        verify(newsRepository, times(1)).save(news);
        verify(cache, times(1)).put(NEWS_ID, news);
        verify(newsMapper, times(1)).toNewsResponseDto(news);
    }

    @Test
    void partialUpdateNews_shouldReturnPartiallyUpdatedNews() {
        News news = new News();
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(news));
        when(newsRepository.save(news)).thenReturn(news);
        when(newsMapper.toNewsResponseDto(news)).thenReturn(UPDATED_NEWS_RESPONSE_DTO);

        NewsResponseDto result = newsService.partialUpdateNews(NEWS_ID, NEWS_PARTIAL_UPDATE_DTO);

        assertNotNull(result);
        assertEquals(UPDATED_NEWS_RESPONSE_DTO.getId(), result.getId());
        assertEquals(NEWS_PARTIAL_UPDATE_DTO.getTitle(), result.getTitle());

        verify(newsRepository, times(1)).findById(NEWS_ID);
        verify(newsRepository, times(1)).save(news);
        verify(newsMapper, times(1)).toNewsResponseDto(news);
    }

    @Test
    void deleteNews_shouldDeleteNews() {
        News news = new News();
        news.setId(NEWS_ID);
        when(newsRepository.existsById(NEWS_ID)).thenReturn(true);
        doNothing().when(newsRepository).deleteById(NEWS_ID);
        doNothing().when(cache).delete(NEWS_ID);

        newsService.deleteNews(NEWS_ID);

        verify(newsRepository, times(1)).existsById(NEWS_ID);
        verify(newsRepository, times(1)).deleteById(NEWS_ID);
        verify(cache, times(1)).delete(NEWS_ID);
    }

    @Test
    void deleteNews_shouldThrowNewsNotFoundException() {
        when(newsRepository.existsById(NEWS_ID)).thenReturn(false);

        assertThrows(NewsNotFoundException.class, () -> newsService.deleteNews(NEWS_ID));

        verify(newsRepository, times(1)).existsById(NEWS_ID);
    }

    @Test
    void searchNews_shouldReturnPageOfSearchResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> newsPage = new PageImpl<>(List.of(new News()));
        when(newsRepository.findByTextContainsIgnoreCaseAndTitleContainsIgnoreCase("test", "title", pageable))
                .thenReturn(newsPage);
        when(newsMapper.toNewsResponseDto(any(News.class))).thenReturn(NEWS_RESPONSE_DTO);

        PageResultDto<NewsResponseDto> result = newsService.searchNews("test", "title", 0, 10);

        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());

        verify(newsRepository, times(1))
                .findByTextContainsIgnoreCaseAndTitleContainsIgnoreCase("test", "title", pageable);
    }

    @Test
    void searchNews_shouldThrowNewsNotFoundException() {
        when(newsRepository.findByTextContainsIgnoreCaseAndTitleContainsIgnoreCase("test", "title", PageRequest.of(0, 10)))
                .thenReturn(Page.empty());

        PageResultDto<NewsResponseDto> result = newsService.searchNews("test", "title", 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }
}
