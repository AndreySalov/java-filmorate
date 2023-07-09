package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.List;

@Service
public class FilmService {
    private final FilmValidation filmValidation = new FilmValidation();
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        List<Film> filmsList = filmStorage.getAllFilms();
        return filmsList;
    }

    public Film updateFilm(Film film) {
        filmValidation.valid(film);
        return filmStorage.updateFilm(film);
    }

    public Film createFilm(Film film) {
        filmValidation.valid(film);
        return filmStorage.create(film);
    }

    public Film getFilm(int filmId) {
        return filmStorage.getFilm(filmId);
    }

    public void addLike(int userId, int filmId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        filmStorage.addLike(film, user);
    }

    public void deleteLike(int userId, int filmId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        filmStorage.deleteLike(film, user);
    }


    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopular(count);
    }
}
