package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationExeption;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController filmController = new FilmController();
    @BeforeEach
    void start(){
        filmController = new FilmController();
    }
    @Test
    void createValidFilm(){
        Film newFilm = new Film("FilmName","Description", LocalDate.now(),100);
        assertNotNull(newFilm.getId());
    }
    @Test
    void createNotValidFilm(){
        assertThrows(ValidationExeption.class,()->filmController.create(new Film("","Description", LocalDate.now(),100)));
        assertThrows(ValidationExeption.class,()->filmController.create(new Film("film","a".repeat(201), LocalDate.now(),100)));
        assertThrows(ValidationExeption.class,()->filmController.create(new Film("film","a", LocalDate.now(),0)));
        assertThrows(ValidationExeption.class,()->filmController.create(new Film("film","a", LocalDate.of(1812,10,5),100)));
    }

}
