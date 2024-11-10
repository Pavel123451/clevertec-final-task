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

/**
 * REST controller for handling requests related to news articles.
 * This controller provides endpoints for creating, retrieving, updating,
 * deleting, and searching news articles, as well as retrieving news with comments.
 */
@RestController
@RequiredArgsConstructor
public class NewsController implements NewsApi {

    private final NewsService newsService;

    /**
     * Creates a new news article.
     *
     * @param newsCreateDto The DTO containing the data for the new news article.
     * @return The response DTO containing the created news article's details.
     */
    @Override
    public NewsResponseDto createNews(NewsCreateDto newsCreateDto) {
        return newsService.createNews(newsCreateDto);
    }

    /**
     * Retrieves a paginated list of news articles.
     *
     * @param page The page number to retrieve.
     * @param size The number of news articles per page.
     * @return A paginated result containing the news articles.
     */
    @Override
    public PageResultDto<NewsResponseDto> getNews(int page, int size) {
        return newsService.getNewsPage(page, size);
    }

    /**
     * Retrieves a specific news article along with its comments.
     *
     * @param newsId The ID of the news article.
     * @param page The page number for comments.
     * @param size The number of comments per page.
     * @return The response DTO containing the news article and its comments.
     */
    @Override
    public NewsWithCommentsResponseDto getNewsWithComments(Long newsId,
                                                           int page,
                                                           int size) {
        return newsService.getNewsWithComments(page, size, newsId);
    }

    /**
     * Updates an existing news article with new data.
     *
     * @param newsId The ID of the news article.
     * @param newsCreateDto The DTO containing the updated data for the news article.
     * @return The response DTO containing the updated news article's details.
     */
    @Override
    public NewsResponseDto updateNews(Long newsId,
                                      NewsCreateDto newsCreateDto) {
        return newsService.updateNews(newsId, newsCreateDto);
    }

    /**
     * Partially updates an existing news article with the provided data.
     *
     * @param newsId The ID of the news article.
     * @param newsPartialUpdateDto The DTO containing the fields to update.
     * @return The response DTO containing the updated news article's details.
     */
    @Override
    public NewsResponseDto partialUpdateNews(Long newsId,
                                             NewsPartialUpdateDto newsPartialUpdateDto) {
        return newsService.partialUpdateNews(newsId, newsPartialUpdateDto);
    }

    /**
     * Deletes a news article by its ID.
     *
     * @param newsId The ID of the news article to delete.
     * @return A message indicating the result of the deletion.
     */
    @Override
    public String deleteNews(Long newsId) {
        newsService.deleteNews(newsId);
        return "News with ID " + newsId + " has been deleted.";
    }

    /**
     * Searches for news articles by title and text.
     *
     * @param text The text to search for in the news articles.
     * @param title The title to search for in the news articles.
     * @param page The page number to retrieve.
     * @param size The number of news articles per page.
     * @return A paginated result containing the news articles matching the search criteria.
     */
    @Override
    public PageResultDto<NewsResponseDto> searchNews(String text, String title, int page, int size) {
        return newsService.searchNews(text, title, page, size);
    }
}

