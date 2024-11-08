package ru.clevertec.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.api.NewsApi;
import ru.clevertec.api.dto.request.NewsCreateDto;
import ru.clevertec.api.dto.request.NewsPartialUpdateDto;
import ru.clevertec.api.dto.response.NewsResponseDto;
import ru.clevertec.api.dto.response.NewsWithCommentsResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;
import ru.clevertec.core.service.NewsService;

@RestController
@RequiredArgsConstructor
public class NewsController implements NewsApi {
    private final NewsService newsService;

    @Override
    public NewsResponseDto createNews(NewsCreateDto newsCreateDto) {
        return newsService.createNews(newsCreateDto);
    }

    @Override
    public PageResultDto<NewsResponseDto> getNews(int page, int size) {
        return newsService.getNewsPage(page, size);
    }

    @Override
    public NewsWithCommentsResponseDto getNewsWithComments(Long newsId,
                                                           int page,
                                                           int size) {
        return newsService.getNewsWithComments(page, size, newsId);
    }

    @Override
    public NewsResponseDto updateNews(Long newsId,
                                      NewsCreateDto newsCreateDto) {
        return newsService.updateNews(newsId, newsCreateDto);
    }

    @Override
    public NewsResponseDto partialUpdateNews(Long newsId,
                                             NewsPartialUpdateDto newsPartialUpdateDto) {
        return newsService.partialUpdateNews(newsId, newsPartialUpdateDto);
    }

    @Override
    public String deleteNews(Long newsId) {
        newsService.deleteNews(newsId);
        return "News with ID " + newsId + " has been deleted.";
    }

    @Override
    public PageResultDto<NewsResponseDto> searchNews(String text, String title, int page, int size) {
        return newsService.searchNews(text, title, page, size);
    }
}
