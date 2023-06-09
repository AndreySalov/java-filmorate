package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAll() {
        final String QUERY = "SELECT GENRE_ID,\n" +
                "       NAME\n" +
                "FROM GENRE";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(QUERY);
        List<Genre> genres = new LinkedList<>();
        while (genreRows.next()) {
            genres.add(createInMemory(genreRows.getInt("GENRE_ID"),
                    genreRows.getString("NAME")));
        }

        return genres;
    }

    public Optional<Genre> getById(Integer id) {
        final String QUERY = "SELECT NAME\n" +
                "FROM GENRE\n" +
                "WHERE GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(QUERY, id);
        if (genreRows.next()) {
            return Optional.of(new Genre(id, genreRows.getString("NAME")));
        } else {
            return Optional.empty();
        }
    }

    private Genre createInMemory(Integer id, String name) {
        return new Genre(id, name);
    }

}