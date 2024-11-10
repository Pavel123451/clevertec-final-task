package ru.clevertec.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.core.model.News;

/**
 * Repository interface for performing CRUD operations on news articles in the database.
 * This interface extends {@link JpaRepository} to provide standard database operations.
 * Additional custom queries for searching and retrieving news articles are also defined.
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Retrieves a paginated list of all news articles.
     *
     * @param pageable The pagination information (page number and page size).
     * @return A paginated list of all news articles.
     */
    Page<News> findAll(Pageable pageable);

    /**
     * Searches for news articles based on the provided text and title.
     * The search is case-insensitive for both the text and the title.
     *
     * @param text The text to search for within the news content.
     * @param title The title to search for within the news articles.
     * @param pageable The pagination information (page number and page size).
     * @return A paginated list of news articles that match the search criteria.
     */
    Page<News> findByTextContainsIgnoreCaseAndTitleContainsIgnoreCase(String text,
                                                                      String title,
                                                                      Pageable pageable);
}

