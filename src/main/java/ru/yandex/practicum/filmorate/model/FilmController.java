package ru.yandex.practicum.filmorate.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final FilmValidation filmValidation = new FilmValidation();
    private int currentId;
    private final static Logger log=LoggerFactory.getLogger(FilmController.class);
    @GetMapping("/films")
    public List<Film> getAllUsers() {
        return new ArrayList<>(films.values());
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        filmValidation.valid(film);
        int newId = getNewId();
        film.setId(newId);
        films.put(newId, film);
        log.info("Добавлен фильм id=" + newId);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateUser(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            filmValidation.valid(film);
            films.put(film.getId(), film);
            log.info("Изменен фильм c id=" + film.getId());
        } else
            throw new ValidationExeption("Фильм с таким id не найден");
        return film;
    }

    private int getNewId() {
        return ++currentId;
    }


}
