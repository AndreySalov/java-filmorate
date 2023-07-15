package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmValidation filmValidation = new FilmValidation();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId;

    @Override
    public Film create(Film film) {
        filmValidation.valid(film);
        int newId = getNewId();
        film.setId(newId);
        films.put(newId, film);
        log.info("Добавлен фильм id=" + newId);
        return film;
    }

    public Film getFilm(Integer filmId) {
        if (films.containsKey(filmId)) {
            return films.get(filmId);
        } else
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            filmValidation.valid(film);
            films.put(film.getId(), film);
            log.info("Изменен фильм c id=" + film.getId());
        } else
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(Film film, User user) {
        film.getLikes().add(user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        film.getLikes().remove(user.getId());
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return null;
    }

    private int getNewId() {
        return ++currentId;
    }

}
