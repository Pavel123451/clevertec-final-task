package ru.clevertec.core.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@TestConfiguration
public abstract class PostgresContainerConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17")
                .withDatabaseName("news_test_db")
                .withUsername("root")
                .withPassword("root")
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("sql/create_tables.sql"),
                        "/docker-entrypoint-initdb.d/init-db.sql");
        return postgreSQLContainer;
    }
}