package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmValidation filmValidation = new FilmValidation();
    @Autowired
    private UserService userService;
    @Autowired
    private FilmStorage inMemoryFilmStorage;

    public Film getFilm(int filmId) {
        Film film = inMemoryFilmStorage.getFilm(filmId);
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> filmsList = inMemoryFilmStorage.getAllFilms();
        return filmsList;
    }

    public Film updateFilm(Film film) {
        filmValidation.valid(film);
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film createFilm(Film film) {
        filmValidation.valid(film);
        return inMemoryFilmStorage.create(film);
    }


    public void addLike(int userId, int filmId) {
        Film film = getFilm(filmId);
        User user = userService.getUser(userId);
        inMemoryFilmStorage.addLike(film, user);
    }

    public void deleteLike(int userId, int filmId) {
        Film film = getFilm(filmId);
        User user = userService.getUser(userId);
        inMemoryFilmStorage.deleteLike(film, user);
    }


    public List<Film> getPopularFilms(int count) {
        List<Film> filmList = new ArrayList<>(inMemoryFilmStorage.getAllFilms());
        return (filmList.stream()
                .sorted(new FilmComparator())
                .limit(count)
                .collect(Collectors.toList()));
    }
}
