package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    @Test
    void validateFilmReleaseDate() {
        UserService userService = new UserService();
        FilmService filmService = new FilmService();
        FilmValidation filmValidation = new FilmValidation();
        FilmController filmController = new FilmController(userService, filmService);
        Film film = new Film(1, "Pirates", "about pirates", LocalDate.of(1894, 5, 8), 124,new HashSet<>());
        assertThrows(ValidationException.class, () -> filmValidation.valid(film));
    }
}