package ru.clevertec.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.api.dto.request.NewsCreateDto;
import ru.clevertec.api.dto.request.NewsPartialUpdateDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;
    private final Cache<Long, News> cache;

    public PageResultDto<NewsResponseDto> getNewsPage(int page, int size) {
        log.debug("Fetching news page: {} with size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findAll(pageable);

        log.debug("Found {} news items", newsPage.getTotalElements());
        return new PageResultDto<>(newsPage.map(newsMapper::toNewsResponseDto));
    }

    public NewsWithCommentsResponseDto getNewsWithComments(int page,
                                                           int size, Long newsId) {
        log.debug("Fetching news with comments for newsId: {} on page: {} with size: {}",
                newsId, page, size);

        News news = cache.get(newsId);
        if (news == null) {
            log.info("Cache miss for newsId: {}, fetching from repository", newsId);
            news = newsRepository.findById(newsId)
                    .orElseThrow(() ->
                            new NewsNotFoundException("News with id " + newsId + " not found"));
            cache.put(newsId, news);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByNewsId(newsId, pageable);

        PageResultDto<CommentResponseDto> commentsResultDto = new PageResultDto<>(
                commentsPage.map(commentMapper::toCommentResponseDto)
        );

        log.debug("Found {} comments for newsId: {}", commentsPage.getTotalElements(), newsId);
        return newsMapper.toNewsWithCommentsResponseDto(news, commentsResultDto);
    }

    public NewsResponseDto createNews(NewsCreateDto newsCreateDto) {
        log.debug("Creating news with data: {}", newsCreateDto);

        News news = newsMapper.toNews(newsCreateDto);
        News savedNews = newsRepository.save(news);
        cache.put(savedNews.getId(), savedNews);

        log.debug("News created with id: {}", savedNews.getId());
        return newsMapper.toNewsResponseDto(savedNews);
    }

    public NewsResponseDto updateNews(Long newsId, NewsCreateDto newsCreateDto) {
        log.debug("Updating news with id: {} with data: {}", newsId, newsCreateDto);

        News news = getNewsFromCacheOrRepository(newsId);

        newsMapper.updateNews(newsCreateDto, news);
        News updatedNews = newsRepository.save(news);
        cache.put(newsId, updatedNews);

        log.debug("News updated with id: {}", newsId);
        return newsMapper.toNewsResponseDto(updatedNews);
    }

    public NewsResponseDto partialUpdateNews(Long newsId,
                                             NewsPartialUpdateDto newsPartialUpdateDto) {
        log.debug("Partially updating news with id: {} with data: {}",
                newsId, newsPartialUpdateDto);

        News news = getNewsFromCacheOrRepository(newsId);

        newsMapper.partialUpdateNews(newsPartialUpdateDto, news);
        News updatedNews = newsRepository.save(news);

        log.debug("News partially updated with ID: {}", newsId);
        return newsMapper.toNewsResponseDto(updatedNews);
    }

    public void deleteNews(Long newsId) {
        log.debug("Deleting news with id: {}", newsId);

        if (!newsRepository.existsById(newsId)) {
            throw new NewsNotFoundException("News with id " + newsId + " not found");
        }
        newsRepository.deleteById(newsId);
        cache.delete(newsId);

        log.debug("News deleted with ID: {}", newsId);
    }

    public PageResultDto<NewsResponseDto> searchNews(String text,
                                                     String title,
                                                     int page, int size) {
        log.debug("Searching news with text: '{}' and title: '{}' on page: {} with size: {}",
                text, title, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository
                .findByTextContainsIgnoreCaseAndTitleContainsIgnoreCase(text, title, pageable);

        log.debug("Found {} news items for search", newsPage.getTotalElements());
        return new PageResultDto<>(newsPage.map(newsMapper::toNewsResponseDto));
    }

    private News getNewsFromCacheOrRepository(Long newsId) {
        News news = cache.get(newsId);
        if (news == null) {
            log.debug("Cache miss for newsId: {}, fetching from repository", newsId);
            news = newsRepository.findById(newsId)
                    .orElseThrow(() -> new NewsNotFoundException("News with id " + newsId + " not found"));
        }
        return news;
    }
}


