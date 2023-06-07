package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final FilmValidation filmValidation = new FilmValidation();
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int currentId;

    @GetMapping("/films")
    public List<Film> getAllUsers() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody @Valid Film film) {
        filmValidation.valid(film);
        int newId = getNewId();
        film.setId(newId);
        films.put(newId, film);
        log.info("Добавлен фильм id=" + newId);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateUser(@RequestBody @Valid Film film) {
        if (films.containsKey(film.getId())) {
            filmValidation.valid(film);
            films.put(film.getId(), film);
            log.info("Изменен фильм c id=" + film.getId());
        } else
            throw new ValidationException("Фильм с таким id не найден");
        return film;
    }

    private int getNewId() {
        return ++currentId;
    }


}
