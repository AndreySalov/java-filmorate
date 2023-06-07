package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.FilmController;



import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    private FilmController filmController = new FilmController();
    private Validator validator;

    @BeforeEach
    void start() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        filmController = new FilmController();
    }

    @Test
    void createValidFilm() {
        Film newFilm = new Film(0, "FilmName", "Description", LocalDate.now(), 100);
        assertNotNull(newFilm.getId());
    }

    @Test
    void createNotValidFilm() {
        Film newFilm = new Film(0, "", "Description", LocalDate.now(), 100);
        Set<ConstraintViolation<Film>> violations = validator.validate(newFilm);
        assertFalse(violations.isEmpty());
        newFilm = new Film(0, "film", "a".repeat(201), LocalDate.now(), 100);
        violations = validator.validate(newFilm);
        assertFalse(violations.isEmpty());
        newFilm = new Film(0, "film", "a", LocalDate.now(), 0);
        violations = validator.validate(newFilm);
        assertFalse(violations.isEmpty());
        assertThrows(Exception.class, () -> filmController.create(new Film(0, "film", "a", LocalDate.of(1812, 10, 5), 100)));
    }

}
