package ru.clevertec.core.data;

import ru.clevertec.api.dto.request.NewsCreateDto;
import ru.clevertec.api.dto.request.NewsPartialUpdateDto;
import ru.clevertec.api.dto.response.NewsResponseDto;

import java.time.ZonedDateTime;

public class NewsTestData {

    public static final NewsCreateDto NEWS_CREATE_DTO = new NewsCreateDto();
    public static final NewsPartialUpdateDto NEWS_PARTIAL_UPDATE_DTO = new NewsPartialUpdateDto();
    public static final NewsResponseDto NEWS_RESPONSE_DTO = new NewsResponseDto();
    public static final NewsResponseDto UPDATED_NEWS_RESPONSE_DTO = new NewsResponseDto();

    public static final String NEWS_CREATE_JSON = """
        {
            "title": "Breaking News: Major Event Happens",
            "text": "Details about the major event that took place today."
        }
    """;

    public static final String NEWS_PARTIAL_UPDATE_JSON = """
        {
            "title": "Updated News Title",
            "text": "This is the updated news content after revision."
        }
    """;

    static {
        NEWS_CREATE_DTO.setTitle("Breaking News: Major Event Happens");
        NEWS_CREATE_DTO.setText("Details about the major event that took place today.");

        NEWS_PARTIAL_UPDATE_DTO.setTitle("Updated News Title");
        NEWS_PARTIAL_UPDATE_DTO.setText("This is the updated news content after revision.");

        NEWS_RESPONSE_DTO.setId(1L);
        NEWS_RESPONSE_DTO.setCreatedAt(ZonedDateTime.now());
        NEWS_RESPONSE_DTO.setTitle("Breaking News: Major Event Happens");
        NEWS_RESPONSE_DTO.setText("Details about the major event that took place today.");

        UPDATED_NEWS_RESPONSE_DTO.setId(1L);
        UPDATED_NEWS_RESPONSE_DTO.setCreatedAt(ZonedDateTime.now());
        UPDATED_NEWS_RESPONSE_DTO.setTitle("Updated News Title");
        UPDATED_NEWS_RESPONSE_DTO.setText("This is the updated news content after revision.");
    }
}

