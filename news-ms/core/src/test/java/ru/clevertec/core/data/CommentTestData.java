package ru.clevertec.core.data;

import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;


import java.time.ZonedDateTime;

public class CommentTestData {

    public static final CommentCreateDto COMMENT_CREATE_DTO = new CommentCreateDto();
    public static final CommentPartialUpdateDto COMMENT_PARTIAL_UPDATE_DTO = new CommentPartialUpdateDto();
    public static final CommentResponseDto COMMENT_RESPONSE_DTO = new CommentResponseDto();
    public static final CommentResponseDto UPDATED_COMMENT_RESPONSE_DTO = new CommentResponseDto();

    public static final String COMMENT_CREATE_JSON = """
        {
            "text": "This is a test comment",
            "username": "pavel1234"
        }
    """;

    public static final String COMMENT_PARTIAL_UPDATE_JSON = """
        {
            "text": "Partially updated text"
        }
    """;

    static {
        COMMENT_CREATE_DTO.setText("This is a test comment");
        COMMENT_CREATE_DTO.setUsername("pavel1234");

        COMMENT_PARTIAL_UPDATE_DTO.setText("Partially updated text");

        COMMENT_RESPONSE_DTO.setId(1L);
        COMMENT_RESPONSE_DTO.setCreatedAt(ZonedDateTime.now());
        COMMENT_RESPONSE_DTO.setText("This is a test comment");
        COMMENT_RESPONSE_DTO.setUsername("pavel1234");

        UPDATED_COMMENT_RESPONSE_DTO.setId(1L);
        UPDATED_COMMENT_RESPONSE_DTO.setCreatedAt(ZonedDateTime.now());
        UPDATED_COMMENT_RESPONSE_DTO.setText("Partially updated text");
        UPDATED_COMMENT_RESPONSE_DTO.setUsername("pavel1234");
    }
}


