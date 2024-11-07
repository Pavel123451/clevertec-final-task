INSERT INTO news (title, text)
VALUES ('News Title 1', 'This is the text of news 1.'),
       ('News Title 2', 'This is the text of news 2.'),
       ('News Title 3', 'This is the text of news 3.'),
       ('News Title 4', 'This is the text of news 4.'),
       ('News Title 5', 'This is the text of news 5.');

INSERT INTO comment (text, username, news_id)
VALUES
    ('Comment 1 for news 1', 'user1', 1),
    ('Comment 2 for news 1', 'user2', 1),
    ('Comment 3 for news 1', 'user3', 1),

    ('Comment 1 for news 2', 'user1', 2),
    ('Comment 2 for news 2', 'user2', 2),
    ('Comment 3 for news 2', 'user3', 2),

    ('Comment 1 for news 3', 'user1', 3),
    ('Comment 2 for news 3', 'user2', 3),
    ('Comment 3 for news 3', 'user3', 3),

    ('Comment 1 for news 4', 'user1', 4),
    ('Comment 2 for news 4', 'user2', 4),
    ('Comment 3 for news 4', 'user3', 4),

    ('Comment 1 for news 5', 'user1', 5),
    ('Comment 2 for news 5', 'user2', 5),
    ('Comment 3 for news 5', 'user3', 5);
