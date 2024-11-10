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
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.core.config.PostgresContainerConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = PostgresContainerConfig.class)
@Sql(scripts = "/sql/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CommentE2ETest {

    private final String uriTemplate = "http://localhost:%s/api/news/%d/comments";
    private final RestClient restClient = RestClient.create();
    @LocalServerPort
    private int port;

    @Test
    public void createComment_shouldReturn201() {
        Long newsId = 1L;
        String uri = String.format(uriTemplate, port, newsId);

        String expectedCommentText = "This is a test comment";
        String expectedUsername = "pavel1234";

        String commentJson = "{\"text\": \"" + expectedCommentText + "\", \"username\": \"" + expectedUsername + "\"}";

        ResponseEntity<CommentResponseDto> response = restClient
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(commentJson)
                .retrieve()
                .toEntity(CommentResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        CommentResponseDto createdComment = response.getBody();

        assertNotNull(createdComment);
        assertEquals(expectedCommentText, createdComment.getText());
        assertEquals(expectedUsername, createdComment.getUsername());
    }

    @Test
    public void getComment_shouldReturn200() {
        Long newsId = 1L;
        Long commentId = 2L;
        String uri = String.format(uriTemplate, port, newsId) + "/" + commentId;

        String expectedUsername = "user2";

        ResponseEntity<CommentResponseDto> response = restClient
                .get()
                .uri(uri)
                .retrieve()
                .toEntity(CommentResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CommentResponseDto retrievedComment = response.getBody();

        assertNotNull(retrievedComment);
        assertEquals(commentId, retrievedComment.getId());
        assertEquals(expectedUsername, retrievedComment.getUsername());
    }

    @Test
    public void partialUpdateComment_shouldReturn200() {
        Long newsId = 1L;
        Long commentId = 1L;
        String uri = String.format(uriTemplate, port, newsId) + "/" + commentId;

        String updatedText = "Partially updated text";

        String partialUpdateJson = "{\"text\": \"" + updatedText + "\"}";

        ResponseEntity<CommentResponseDto> response = restClient
                .patch()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(partialUpdateJson)
                .retrieve()
                .toEntity(CommentResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CommentResponseDto updatedComment = response.getBody();

        assertNotNull(updatedComment);
        assertEquals(commentId, updatedComment.getId());
        assertEquals(updatedText, updatedComment.getText());
        assertEquals("user1", updatedComment.getUsername());
    }

    @Test
    public void updateComment_shouldReturn200() {
        Long newsId = 1L;
        Long commentId = 1L;
        String uri = String.format(uriTemplate, port, newsId) + "/" + commentId;

        String updatedText = "Updated full comment text";

        String fullUpdateJson = "{\"text\": \"" + updatedText + "\", \"username\": \"pavel1234\"}";

        ResponseEntity<CommentResponseDto> response = restClient
                .put()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fullUpdateJson)
                .retrieve()
                .toEntity(CommentResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CommentResponseDto updatedComment = response.getBody();

        assertNotNull(updatedComment);
        assertEquals(commentId, updatedComment.getId());
        assertEquals(updatedText, updatedComment.getText());
        assertEquals("pavel1234", updatedComment.getUsername());
    }

    @Test
    public void deleteComment_shouldReturn200() {
        Long newsId = 1L;
        Long commentId = 1L;
        String uri = String.format(uriTemplate, port, newsId) + "/" + commentId;

        String expectedResponse = "Comment with id 1 has been deleted.";

        ResponseEntity<String> response = restClient
                .delete()
                .uri(uri)
                .retrieve()
                .toEntity(String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}



