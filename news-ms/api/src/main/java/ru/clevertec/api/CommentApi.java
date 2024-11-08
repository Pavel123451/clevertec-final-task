package ru.clevertec.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;

@RestController
@RequestMapping("/api/news/{newsId}/comments")
public interface CommentApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentResponseDto createComment(@PathVariable Long newsId,
                                     @Valid @RequestBody CommentCreateDto commentCreateDto);

    @GetMapping("/{commentId}")
    CommentResponseDto getComment(@PathVariable Long newsId, @PathVariable Long commentId);


    @PutMapping("/{commentId}")
    CommentResponseDto updateComment(@PathVariable Long newsId,
                                     @PathVariable Long commentId,
                                     @Valid @RequestBody CommentCreateDto commentCreateDto);

    @PatchMapping("/{commentId}")
    CommentResponseDto partialUpdateComment(
            @PathVariable Long newsId,
            @PathVariable Long commentId,
            @RequestBody CommentPartialUpdateDto commentPartialUpdateDto
    );

    @DeleteMapping("/{commentId}")
    String deleteComment(@PathVariable Long newsId,
                         @PathVariable Long commentId);

    @GetMapping("/search")
    public PageResultDto<CommentResponseDto> searchComments(
            @PathVariable Long newsId,
            @RequestParam String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );
}
