package ru.clevertec.api;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.api.dto.request.CommentCreateDto;
import ru.clevertec.api.dto.request.CommentPartialUpdateDto;
import ru.clevertec.api.dto.response.CommentResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;

@RestController
@RequestMapping("/api/news/{newsId}/comments")
public interface CommentApi {

    @Operation(
            summary = "Create a comment for a news article",
            description = "Creates a new comment associated with a specific news article.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Comment successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentResponseDto createComment(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @Valid
            @RequestBody
            @Parameter(description = "Comment details", required = true)
            CommentCreateDto commentCreateDto
    );

    @Operation(
            summary = "Get a specific comment",
            description = "Retrieve a comment by its ID for a specific news article.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Comment not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/{commentId}")
    CommentResponseDto getComment(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @Parameter(description = "ID of the comment", required = true)
            @PathVariable Long commentId
    );

    @Operation(
            summary = "Update a specific comment",
            description = "Update the entire content of an existing comment.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Comment not found",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping("/{commentId}")
    CommentResponseDto updateComment(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @Parameter(description = "ID of the comment", required = true)
            @PathVariable Long commentId,

            @Valid
            @RequestBody
            @Parameter(description = "Updated comment details", required = true)
            CommentCreateDto commentCreateDto
    );

    @Operation(
            summary = "Partially update a comment",
            description = "Update specific fields of an existing comment.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment partially updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Comment not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PatchMapping("/{commentId}")
    CommentResponseDto partialUpdateComment(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @Parameter(description = "ID of the comment", required = true)
            @PathVariable Long commentId,

            @RequestBody
            @Parameter(description = "Partial update details", required = true)
            CommentPartialUpdateDto commentPartialUpdateDto
    );

    @Operation(
            summary = "Delete a comment",
            description = "Deletes a specific comment by its ID for a given news article.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully deleted",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Comment not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping("/{commentId}")
    String deleteComment(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @Parameter(description = "ID of the comment", required = true)
            @PathVariable Long commentId
    );

    @Operation(
            summary = "Search comments by text",
            description = "Searches for comments containing the specified text.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageResultDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid query parameters",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/search")
    PageResultDto<CommentResponseDto> searchComments(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @Parameter(description = "Text to search for in comments", required = true)
            @RequestParam String text,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size
    );
}
