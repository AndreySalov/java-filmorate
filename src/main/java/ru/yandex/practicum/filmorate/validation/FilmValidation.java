package ru.yandex.practicum.filmorate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidation {
    static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    private final Logger log = LoggerFactory.getLogger(FilmValidation.class);

    public void valid(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            log.info("Ошибка при проверка фильма. Неверная дата релиза");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
