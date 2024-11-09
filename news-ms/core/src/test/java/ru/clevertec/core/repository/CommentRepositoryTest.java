package ru.clevertec.core.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.clevertec.core.config.PostgresContainerConfig;
import ru.clevertec.core.model.Comment;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = PostgresContainerConfig.class)
@ActiveProfiles("test")
@Sql(scripts = "/sql/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testFindByNewsId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentRepository.findByNewsId(1L, pageable);

        assertNotNull(comments);
        assertEquals(3, comments.getTotalElements());
    }

    @Test
    void testFindByTextContainsIgnoreCaseAndNewsId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentRepository
                .findByTextContainsIgnoreCaseAndNewsId("Comment 1", pageable, 1L);

        assertNotNull(comments);
        assertEquals(1, comments.getTotalElements());
    }

    @Test
    void testFindByNewsIdAndId() {
        List<Comment> allComments = commentRepository.findAll();
        allComments.forEach(comment -> System.out.println(comment.getId()
                + " : " + comment.getNews().getId() + " : " + comment.getText()));

        Optional<Comment> comment = commentRepository.findByNewsIdAndId(1L, 1L);

        assertTrue(comment.isPresent());
        assertEquals(1L, comment.get().getId());
    }
}

