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

/**
 * Service class for handling the business logic related to news articles.
 * It provides methods for creating, retrieving, updating, deleting, and searching news articles.
 * Operations are cached to improve performance.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;
    private final Cache<Long, News> cache;

    /**
     * Retrieves a paginated list of news articles.
     *
     * @param page The page number to retrieve.
     * @param size The number of news articles per page.
     * @return A paginated result containing the news articles.
     */
    public PageResultDto<NewsResponseDto> getNewsPage(int page, int size) {
        log.debug("Fetching news page: {} with size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findAll(pageable);

        log.debug("Found {} news items", newsPage.getTotalElements());
        return new PageResultDto<>(newsPage.map(newsMapper::toNewsResponseDto));
    }

    /**
     * Retrieves news along with comments for a specific news article.
     *
     * @param page The page number for comments.
     * @param size The number of comments per page.
     * @param newsId The ID of the news article to retrieve.
     * @return A response DTO containing news with its comments.
     * @throws NewsNotFoundException if the news article does not exist.
     */
    public NewsWithCommentsResponseDto getNewsWithComments(int page,
                                                           int size, Long newsId) {
        log.debug("Fetching news with comments for newsId: {} on page: {} with size: {}",
                newsId, page, size);

        News news = cache.get(newsId);
        if (news == null) {
            log.info("Cache miss for newsId: {}, fetching from repository", newsId);
            news = newsRepository.findById(newsId)
                    .orElseThrow(() -> new NewsNotFoundException("News with id " + newsId + " not found"));
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

    /**
     * Creates a new news article.
     *
     * @param newsCreateDto The DTO containing the data of the news article to create.
     * @return A response DTO containing the created news article.
     */
    public NewsResponseDto createNews(NewsCreateDto newsCreateDto) {
        log.debug("Creating news with data: {}", newsCreateDto);

        News news = newsMapper.toNews(newsCreateDto);
        News savedNews = newsRepository.save(news);
        cache.put(savedNews.getId(), savedNews);

        log.debug("News created with id: {}", savedNews.getId());
        return newsMapper.toNewsResponseDto(savedNews);
    }

    /**
     * Updates an existing news article.
     *
     * @param newsId The ID of the news article to update.
     * @param newsCreateDto The DTO containing the updated data of the news article.
     * @return A response DTO containing the updated news article.
     * @throws NewsNotFoundException if the news article does not exist.
     */
    public NewsResponseDto updateNews(Long newsId, NewsCreateDto newsCreateDto) {
        log.debug("Updating news with id: {} with data: {}", newsId, newsCreateDto);

        News news = getNewsFromCacheOrRepository(newsId);

        newsMapper.updateNews(newsCreateDto, news);
        News updatedNews = newsRepository.save(news);
        cache.put(newsId, updatedNews);

        log.debug("News updated with id: {}", newsId);
        return newsMapper.toNewsResponseDto(updatedNews);
    }

    /**
     * Partially updates an existing news article.
     *
     * @param newsId The ID of the news article to update.
     * @param newsPartialUpdateDto The DTO containing the updated fields of the news article.
     * @return A response DTO containing the updated news article.
     * @throws NewsNotFoundException if the news article does not exist.
     */
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

    /**
     * Deletes an existing news article.
     *
     * @param newsId The ID of the news article to delete.
     * @throws NewsNotFoundException if the news article does not exist.
     */
    public void deleteNews(Long newsId) {
        log.debug("Deleting news with id: {}", newsId);

        if (!newsRepository.existsById(newsId)) {
            throw new NewsNotFoundException("News with id " + newsId + " not found");
        }
        newsRepository.deleteById(newsId);
        cache.delete(newsId);

        log.debug("News deleted with ID: {}", newsId);
    }

    /**
     * Searches news articles based on text and title.
     *
     * @param text The text to search within the news articles.
     * @param title The title to search within the news articles.
     * @param page The page number to retrieve.
     * @param size The number of news articles per page.
     * @return A paginated result containing the matching news articles.
     */
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

    /**
     * Retrieves a news article from cache or repository.
     *
     * @param newsId The ID of the news article to retrieve.
     * @return The news article.
     * @throws NewsNotFoundException if the news article does not exist.
     */
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



