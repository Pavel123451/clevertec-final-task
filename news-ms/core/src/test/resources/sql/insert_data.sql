INSERT INTO news (title, text)
VALUES ('News Title 1', 'This is the text of news 1.'),
       ('News Title 2', 'This is the text of news 2.');

INSERT INTO comment (text, username, news_id)
VALUES
    ('Comment 1 for news 1', 'user1', 1),
    ('Comment 2 for news 1', 'user2', 1),
    ('Comment 3 for news 1', 'user3', 1),

    ('Comment 1 for news 2', 'user1', 2),
    ('Comment 2 for news 2', 'user2', 2),
    ('Comment 3 for news 2', 'user3', 2);