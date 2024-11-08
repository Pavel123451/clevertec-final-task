package ru.clevertec.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.core.model.Comment;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByNewsId(Long newsId, Pageable pageable);

    Page<Comment> findByTextContainsIgnoreCaseAndNewsId(String text,
                                                        Pageable pageable,
                                                        Long newsId);

    Optional<Comment> findByNewsIdAndId(Long newsId, Long id);
}
