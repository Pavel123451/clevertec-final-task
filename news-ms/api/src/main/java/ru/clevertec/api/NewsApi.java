package ru.clevertec.api;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.api.dto.request.NewsCreateDto;
import ru.clevertec.api.dto.request.NewsPartialUpdateDto;
import ru.clevertec.api.dto.response.NewsResponseDto;
import ru.clevertec.api.dto.response.NewsWithCommentsResponseDto;
import ru.clevertec.api.dto.response.PageResultDto;

@RestController
@RequestMapping("/api/news")
public interface NewsApi {

    @Operation(
            summary = "Create a news article",
            description = "Creates a new news article.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "News article successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NewsResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    NewsResponseDto createNews(
            @Valid
            @RequestBody
            @Parameter(description = "News article details", required = true)
            NewsCreateDto newsCreateDto
    );

    @Operation(
            summary = "Get a list of news articles",
            description = "Retrieve a paginated list of news articles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "News articles found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageResultDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid query parameters",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    PageResultDto<NewsResponseDto> getNews(
            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(
            summary = "Get news article with comments",
            description = "Retrieve a news article along with its associated comments.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "News article with comments found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NewsWithCommentsResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "News article not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/{newsId}/comments")
    NewsWithCommentsResponseDto getNewsWithComments(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(
            summary = "Update a news article",
            description = "Update the details of an existing news article.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "News article successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NewsResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "News article not found",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping("/{newsId}")
    NewsResponseDto updateNews(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @Valid
            @RequestBody
            @Parameter(description = "Updated news article details", required = true)
            NewsCreateDto newsCreateDto
    );

    @Operation(
            summary = "Partially update a news article",
            description = "Update specific fields of an existing news article.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "News article partially updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NewsResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "News article not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PatchMapping("/{newsId}")
    NewsResponseDto partialUpdateNews(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId,

            @RequestBody
            @Parameter(description = "Partial update details", required = true)
            NewsPartialUpdateDto newsPartialUpdateDto
    );

    @Operation(
            summary = "Delete a news article",
            description = "Delete an existing news article.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "News article successfully deleted",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "News article not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping("/{newsId}")
    String deleteNews(
            @Parameter(description = "ID of the news article", required = true)
            @PathVariable Long newsId
    );

    @Operation(
            summary = "Search for news articles",
            description = "Search for news articles by text and title.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "News articles found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageResultDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid query parameters",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/search")
    PageResultDto<NewsResponseDto> searchNews(
            @Parameter(description = "Search text", required = true)
            @RequestParam String text,

            @Parameter(description = "Search title", required = true)
            @RequestParam String title,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size
    );
}
