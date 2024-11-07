package ru.clevertec.api;

import jakarta.validation.Valid;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    NewsResponseDto createNews(@Valid @RequestBody NewsCreateDto newsCreateDto);

    @GetMapping
    PageResultDto<NewsResponseDto> getNews(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size);

    @GetMapping("/{newsId}")
    NewsWithCommentsResponseDto getNewsWithComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long newsId
    );

    @PutMapping("/{newsId}")
    NewsResponseDto updateNews(@PathVariable Long newsId,
                               @Valid @RequestBody NewsCreateDto newsCreateDto);

    @PatchMapping("/{newsId}")
    NewsResponseDto partialUpdateNews(@PathVariable Long newsId,
                                      @RequestBody NewsPartialUpdateDto newsPartialUpdateDto);

    @DeleteMapping("/{newsId}")
    String deleteNews(@PathVariable Long newsId);

    @GetMapping("/search")
    public PageResultDto<NewsResponseDto> searchNews(@RequestParam String text,
                                                     @RequestParam String title,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size);

}
