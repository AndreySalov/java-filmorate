package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

class FilmControllerTest {
    @Test
    void validateFilmReleaseDate() {
        FilmValidation filmValidation = new FilmValidation();
//        Film film = new Film(1, "Pirates", "about pirates", LocalDate.of(1894, 5, 8), 124);
        //assertThrows(ValidationException.class, () -> filmValidation.valid(film));
    }
}