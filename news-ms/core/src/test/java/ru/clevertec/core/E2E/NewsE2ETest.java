package ru.clevertec.core.E2E;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;
import ru.clevertec.api.dto.request.NewsCreateDto;
import ru.clevertec.api.dto.request.NewsPartialUpdateDto;
import ru.clevertec.api.dto.response.NewsResponseDto;
import ru.clevertec.core.config.PostgresContainerConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = PostgresContainerConfig.class)
@Sql(scripts = "/sql/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class NewsE2ETest {


    private final String uriTemplate = "http://localhost:%s/api/news";
    private final RestClient restClient = RestClient.create();
    @LocalServerPort
    private int port;

    @Test
    public void createNews_shouldReturn201() {
        String uri = String.format(uriTemplate, port);

        NewsCreateDto newsCreateDto = new NewsCreateDto();
        newsCreateDto.setTitle("Breaking News: Major Event Happens");
        newsCreateDto.setText("Details about the major event that took place today.");

        String newsJson = "{\"title\": \"" + newsCreateDto.getTitle() + "\", \"text\": \"" + newsCreateDto.getText() + "\"}";

        ResponseEntity<NewsResponseDto> response = restClient
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(newsJson)
                .retrieve()
                .toEntity(NewsResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        NewsResponseDto createdNews = response.getBody();

        assertNotNull(createdNews);
        assertEquals(newsCreateDto.getTitle(), createdNews.getTitle());
        assertEquals(newsCreateDto.getText(), createdNews.getText());
    }

    @Test
    public void updateNews_shouldReturn200() {
        Long newsId = 1L;
        String uri = String.format(uriTemplate + "/%d", port, newsId);

        NewsCreateDto updatedNews = new NewsCreateDto();
        updatedNews.setTitle("Updated News Title");
        updatedNews.setText("This is the updated news content after revision.");

        String updatedNewsJson = "{\"title\": \"" + updatedNews.getTitle() + "\", \"text\": \"" + updatedNews.getText() + "\"}";

        ResponseEntity<NewsResponseDto> response = restClient
                .put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedNewsJson)
                .retrieve()
                .toEntity(NewsResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        NewsResponseDto updatedNewsResponse = response.getBody();

        assertNotNull(updatedNewsResponse);
        assertEquals(updatedNews.getTitle(), updatedNewsResponse.getTitle());
        assertEquals(updatedNews.getText(), updatedNewsResponse.getText());
    }

    @Test
    public void partialUpdateNews_shouldReturn200() {
        Long newsId = 1L;
        String uri = String.format(uriTemplate + "/%d", port, newsId);

        NewsPartialUpdateDto partialUpdate = new NewsPartialUpdateDto();
        partialUpdate.setText("This is the updated news content after revision.");

        String partialUpdateJson = "{\"text\": \"" + partialUpdate.getText() + "\"}";

        ResponseEntity<NewsResponseDto> response = restClient
                .patch()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(partialUpdateJson)
                .retrieve()
                .toEntity(NewsResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        NewsResponseDto updatedNewsResponse = response.getBody();

        assertNotNull(updatedNewsResponse);
        assertEquals(partialUpdate.getText(), updatedNewsResponse.getText());
    }

    @Test
    public void deleteNews_shouldReturn200() {
        Long newsId = 1L;
        String uri = String.format(uriTemplate + "/%d", port, newsId);

        String expectedResponse = "News with ID 1 has been deleted.";

        ResponseEntity<String> response = restClient
                .delete()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}

