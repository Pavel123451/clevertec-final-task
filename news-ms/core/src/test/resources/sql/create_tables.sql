CREATE TABLE news
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    title      VARCHAR(255)             NOT NULL,
    text       TEXT                     NOT NULL
);

CREATE TABLE comment
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    text       TEXT                     NOT NULL,
    username   VARCHAR(255)             NOT NULL,
    news_id    BIGINT                   NOT NULL,
    CONSTRAINT fk_comment_news FOREIGN KEY (news_id) REFERENCES news (id) ON DELETE CASCADE
);