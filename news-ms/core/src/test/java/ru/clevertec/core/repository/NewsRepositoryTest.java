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
import ru.clevertec.core.model.News;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = PostgresContainerConfig.class)
@ActiveProfiles("test")
@Sql(scripts = "/sql/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clean_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> newsPage = newsRepository.findAll(pageable);

        assertNotNull(newsPage);
        assertEquals(2, newsPage.getTotalElements());
    }

    @Test
    void testFindByTextContainsIgnoreCaseAndTitleContainsIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> newsPage = newsRepository
                .findByTextContainsIgnoreCaseAndTitleContainsIgnoreCase("news 1", "title 1", pageable);

        assertNotNull(newsPage);
        assertEquals(1, newsPage.getTotalElements());
    }
}
