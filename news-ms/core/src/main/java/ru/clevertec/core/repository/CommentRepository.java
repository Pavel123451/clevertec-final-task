package ru.clevertec.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.core.model.Comment;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on comments in the database.
 * This interface extends {@link JpaRepository} to provide standard database operations.
 * Additional custom queries for searching and retrieving comments are also defined.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Retrieves a paginated list of comments associated with a specific news article.
     *
     * @param newsId The ID of the news article to which the comments belong.
     * @param pageable The pagination information (page number and page size).
     * @return A paginated list of comments for the specified news article.
     */
    Page<Comment> findByNewsId(Long newsId, Pageable pageable);

    /**
     * Searches for comments containing the specified text within a specific news article.
     * The search is case-insensitive.
     *
     * @param text The text to search for within the comments.
     * @param pageable The pagination information (page number and page size).
     * @param newsId The ID of the news article to search within.
     * @return A paginated list of comments containing the specified text for the specified news article.
     */
    Page<Comment> findByTextContainsIgnoreCaseAndNewsId(String text,
                                                        Pageable pageable,
                                                        Long newsId);

    /**
     * Retrieves a specific comment by its ID and the associated news article ID.
     *
     * @param newsId The ID of the news article to which the comment belongs.
     * @param id The ID of the comment to retrieve.
     * @return An optional containing the comment if found, or empty if not found.
     */
    Optional<Comment> findByNewsIdAndId(Long newsId, Long id);
}

