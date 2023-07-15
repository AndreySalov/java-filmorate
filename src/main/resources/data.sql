INSERT INTO GENRE (GENRE_ID, NAME)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

INSERT  INTO MPA  (MPA_ID, NAME)
SELECT 1, 'G'
UNION
SELECT 2, 'PG'
UNION
SELECT 3, 'PG-13'
UNION
SELECT 4, 'R'
UNION
SELECT 5, 'NC-17';
