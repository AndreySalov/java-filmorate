package ru.yandex.practicum.filmorate.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class FilmValidation {
    private final Logger log = LoggerFactory.getLogger(FilmValidation.class);

    public void valid(Film film) throws ValidationExeption {
        if (film.getName().length() == 0) {
            log.info("Ошибка при проверка фильма. Название фльма пустое");
            throw new ValidationExeption("Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.info("Ошибка при проверка фильма. Слишком дленное описание фильма.");
            throw new ValidationExeption("Описание фильма не может быть длиннее двухсот символов.");
        } else if (film.getDuration() <= 0) {
            log.info("Ошибка при проверка фильма. Длительность фильма меньше или равна нулю");
            throw new ValidationExeption("Длительность фильма должна быть положительной");
        }
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(minDate)) {
            log.info("Ошибка при проверка фильма. Неверная дата релиза");
            throw new ValidationExeption("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
