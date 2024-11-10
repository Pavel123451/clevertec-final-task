package ru.clevertec.core.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;
import ru.clevertec.core.service.CommentService;
import ru.clevertec.exceptionhandlestarter.config.ExceptionHandlerAutoConfiguration;
import ru.clevertec.exceptionhandlestarter.exception.CommentNotFoundException;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.clevertec.core.data.CommentTestData.*;

@WebMvcTest(CommentController.class)
@Import(ExceptionHandlerAutoConfiguration.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void createComment_shouldReturn201() throws Exception {
        Mockito.when(commentService.createComment(anyLong(), any()))
                .thenReturn(COMMENT_RESPONSE_DTO);

        mockMvc.perform(post("/api/news/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(COMMENT_CREATE_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value(COMMENT_CREATE_DTO.getText()))
                .andExpect(jsonPath("$.username").value(COMMENT_CREATE_DTO.getUsername()));
    }

    @Test
    void getComment_shouldReturn200_whenCommentExists() throws Exception {
        Mockito.when(commentService.getComment(anyLong(), anyLong()))
                .thenReturn(COMMENT_RESPONSE_DTO);

        mockMvc.perform(get("/api/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value(COMMENT_RESPONSE_DTO.getText()));
    }

    @Test
    void getComment_shouldReturn404_whenCommentDoesNotExist() throws Exception {
        Mockito.when(commentService.getComment(anyLong(), anyLong()))
                .thenThrow(new CommentNotFoundException("Comment not found"));

        mockMvc.perform(get("/api/news/1/comments/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateComment_shouldReturn200() throws Exception {
        Mockito.when(commentService.updateComment(anyLong(), anyLong(), any()))
                .thenReturn(COMMENT_RESPONSE_DTO);

        mockMvc.perform(put("/api/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(COMMENT_CREATE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(COMMENT_CREATE_DTO.getText()))
                .andExpect(jsonPath("$.username").value(COMMENT_CREATE_DTO.getUsername()));
    }

    @Test
    void partialUpdateComment_shouldReturn200() throws Exception {
        Mockito.when(commentService.partialUpdateComment(anyLong(), anyLong(), any()))
                .thenReturn(UPDATED_COMMENT_RESPONSE_DTO);

        mockMvc.perform(patch("/api/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(COMMENT_PARTIAL_UPDATE_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(COMMENT_PARTIAL_UPDATE_DTO.getText()));
    }

    @Test
    void deleteComment_shouldReturn200() throws Exception {
        Mockito.when(commentService.deleteComment(anyLong(), anyLong()))
                .thenReturn("Comment with id 1 has been deleted.");

        mockMvc.perform(delete("/api/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment with id 1 has been deleted."));
    }

    @Test
    void searchComments_shouldReturn200_withValidText() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CommentResponseDto> commentPage =
                new PageImpl<>(List.of(COMMENT_RESPONSE_DTO), pageRequest, 1);

        Mockito.when(commentService.searchComments(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(new PageResultDto<>(commentPage));

        mockMvc.perform(get("/api/news/1/comments/search")
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

}

