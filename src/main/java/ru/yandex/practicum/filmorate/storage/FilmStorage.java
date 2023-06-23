package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.util.HashMap;
import java.util.List;

public interface FilmStorage {
    //public HashMap<Integer, Film> GetFilms();
    Film getFilm(Integer filmId);

    Film create(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);
}
