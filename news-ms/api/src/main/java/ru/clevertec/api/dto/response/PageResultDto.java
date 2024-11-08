package ru.clevertec.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResultDto<T> {

    @Schema(description = "List of content items for the current page")
    private List<T> content;

    @Schema(description = "Total number of elements across all pages", example = "100")
    private long totalElements;

    @Schema(description = "Total number of pages available", example = "10")
    private int totalPages;

    @Schema(description = "Current page number", example = "1")
    private int pageNumber;

    @Schema(description = "Size of each page", example = "10")
    private int pageSize;

    public PageResultDto(Page<T> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
    }
}
