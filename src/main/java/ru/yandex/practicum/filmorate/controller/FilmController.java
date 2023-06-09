package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;


@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final UserService userService;
    private final FilmService filmService;
    private Integer count;

    @Autowired
    public FilmController(UserService userService, FilmService filmService) {
        this.userService = userService;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = filmService.getAllFilms();
        log.debug("Количество фильмов :" + filmsList.size());
        return filmsList;
    }

    @GetMapping("/{filmId}")
    Film getFilm(@PathVariable int filmId) {
        return filmService.getFilm(filmId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        Film savedFilm = filmService.createFilm(film);
        log.debug("Добавлен фильм :" + savedFilm);
        return savedFilm;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        Film savedFilm = filmService.updateFilm(film);
        log.debug("Изменен фильм :" + savedFilm);
        return savedFilm;
    }

    @GetMapping(value = "/popular")
    @ResponseBody
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10") @Positive Integer count) {
        Integer countValid = count;
        return filmService.getPopularFilms(countValid);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable int userId, @PathVariable int filmId) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable int userId, @PathVariable int filmId) {
        filmService.deleteLike(userId, filmId);
    }
}

