package ru.clevertec.core.service;

import lombok.RequiredArgsConstructor;
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
import ru.clevertec.core.exception.NewsNotFoundException;
import ru.clevertec.core.mapper.CommentMapper;
import ru.clevertec.core.mapper.NewsMapper;
import ru.clevertec.core.model.Comment;
import ru.clevertec.core.model.News;
import ru.clevertec.core.repository.CommentRepository;
import ru.clevertec.core.repository.NewsRepository;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;
    private final Cache<Long, News> cache;

    public PageResultDto<NewsResponseDto> getNewsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository.findAll(pageable);
        return new PageResultDto<>(newsPage.map(newsMapper::toNewsResponseDto));
    }

    public NewsWithCommentsResponseDto getNewsWithComments(int page,
                                                           int size,
                                                           Long newsId) {
        News news = cache.get(newsId);
        if (news == null) {
            news = newsRepository.findById(newsId)
                    .orElseThrow(() -> new NewsNotFoundException("News with id " + newsId + " not found"));
            cache.put(newsId, news);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByNewsId(newsId, pageable);

        PageResultDto<CommentResponseDto> commentsResultDto = new PageResultDto<>(
                commentsPage.map(commentMapper::toCommentResponseDto)
        );

        return newsMapper.toNewsWithCommentsResponseDto(news, commentsResultDto);
    }

    public NewsResponseDto createNews(NewsCreateDto newsCreateDto) {
        News news = newsMapper.toNews(newsCreateDto);
        News savedNews = newsRepository.save(news);
        cache.put(savedNews.getId(), savedNews); // Сохраняем в кэш

        return newsMapper.toNewsResponseDto(savedNews);
    }

    public NewsResponseDto updateNews(Long newsId, NewsCreateDto newsCreateDto) {
        News news = getNewsFromCacheOrRepository(newsId);

        newsMapper.updateNews(newsCreateDto, news);
        News updatedNews = newsRepository.save(news);
        cache.put(newsId, updatedNews);

        return newsMapper.toNewsResponseDto(updatedNews);
    }

    public NewsResponseDto partialUpdateNews(Long newsId,
                                             NewsPartialUpdateDto newsPartialUpdateDto) {
        News news = getNewsFromCacheOrRepository(newsId);

        newsMapper.partialUpdateNews(newsPartialUpdateDto, news);
        News updatedNews = newsRepository.save(news);
        return newsMapper.toNewsResponseDto(updatedNews);
    }

    public void deleteNews(Long newsId) {
        if (!newsRepository.existsById(newsId)) {
            throw new NewsNotFoundException("News with id " + newsId + " not found");
        }
        newsRepository.deleteById(newsId);
        cache.delete(newsId);
    }


    public PageResultDto<NewsResponseDto> searchNews(String text,
                                                     String title,
                                                     int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<News> newsPage = newsRepository
                .findByTextContainsIgnoreCaseAndTitleContainsIgnoreCase(text, title, pageable);

        return new PageResultDto<>(newsPage.map(newsMapper::toNewsResponseDto));
    }

    private News getNewsFromCacheOrRepository(Long newsId) {
        News news = cache.get(newsId);
        if (news == null) {
            news = newsRepository.findById(newsId)
                    .orElseThrow(() -> new NewsNotFoundException("News with id " + newsId + " not found"));
        }
        return news;
    }
}

