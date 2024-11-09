package ru.clevertec.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.api.dto.request.NewsCreateDto;
import ru.clevertec.api.dto.request.NewsPartialUpdateDto;
import ru.clevertec.api.dto.response.NewsResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;
import ru.clevertec.core.service.NewsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.clevertec.core.data.NewsTestData.*;

@WebMvcTest(NewsController.class)
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createNews_shouldReturn201() throws Exception {
        Mockito.when(newsService.createNews(any(NewsCreateDto.class)))
                .thenReturn(NEWS_RESPONSE_DTO);

        mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NEWS_CREATE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(NEWS_RESPONSE_DTO.getId()))
                .andExpect(jsonPath("$.title").value(NEWS_RESPONSE_DTO.getTitle()))
                .andExpect(jsonPath("$.text").value(NEWS_RESPONSE_DTO.getText()));
    }

    @Test
    void getNews_shouldReturn200() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<NewsResponseDto> page = new PageImpl<>(List.of(NEWS_RESPONSE_DTO), pageRequest, 1);
        PageResultDto<NewsResponseDto> pageResultDto = new PageResultDto<>(page);

        Mockito.when(newsService.getNewsPage(anyInt(), anyInt()))
                .thenReturn(pageResultDto);

        mockMvc.perform(get("/api/news")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(NEWS_RESPONSE_DTO.getId()))
                .andExpect(jsonPath("$.content[0].title").value(NEWS_RESPONSE_DTO.getTitle()))
                .andExpect(jsonPath("$.content[0].text").value(NEWS_RESPONSE_DTO.getText()));
    }

    @Test
    void updateNews_shouldReturn200() throws Exception {
        Mockito.when(newsService.updateNews(anyLong(), any(NewsCreateDto.class)))
                .thenReturn(UPDATED_NEWS_RESPONSE_DTO);

        mockMvc.perform(put("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NEWS_CREATE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(UPDATED_NEWS_RESPONSE_DTO.getId()))
                .andExpect(jsonPath("$.title").value(UPDATED_NEWS_RESPONSE_DTO.getTitle()))
                .andExpect(jsonPath("$.text").value(UPDATED_NEWS_RESPONSE_DTO.getText()));
    }

    @Test
    void partialUpdateNews_shouldReturn200() throws Exception {
        Mockito.when(newsService.partialUpdateNews(anyLong(), any(NewsPartialUpdateDto.class)))
                .thenReturn(UPDATED_NEWS_RESPONSE_DTO);

        mockMvc.perform(patch("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(NEWS_PARTIAL_UPDATE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(UPDATED_NEWS_RESPONSE_DTO.getId()))
                .andExpect(jsonPath("$.title").value(UPDATED_NEWS_RESPONSE_DTO.getTitle()))
                .andExpect(jsonPath("$.text").value(UPDATED_NEWS_RESPONSE_DTO.getText()));
    }

    @Test
    void deleteNews_shouldReturn200() throws Exception {
        String message = "News with ID 1 has been deleted.";
        Mockito.doNothing().when(newsService).deleteNews(anyLong());

        mockMvc.perform(delete("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    @Test
    void searchNews_shouldReturn200_withValidSearch() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<NewsResponseDto> page = new PageImpl<>(List.of(NEWS_RESPONSE_DTO), pageRequest, 1);
        PageResultDto<NewsResponseDto> pageResultDto = new PageResultDto<>(page);

        Mockito.when(newsService.searchNews(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(pageResultDto);

        mockMvc.perform(get("/api/news/search")
                        .param("text", "Details")
                        .param("title", "Breaking News")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(NEWS_RESPONSE_DTO.getId()))
                .andExpect(jsonPath("$.content[0].title").value(NEWS_RESPONSE_DTO.getTitle()))
                .andExpect(jsonPath("$.content[0].text").value(NEWS_RESPONSE_DTO.getText()));
    }

    @Test
    void getNewsWithComments_shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/news/1/comments")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.news.id").value(NEWS_RESPONSE_DTO.getId()))
                .andExpect(jsonPath("$.news.title").value(NEWS_RESPONSE_DTO.getTitle()));
    }
}
