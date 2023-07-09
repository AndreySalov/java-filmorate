package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAll() {
        final String QUERY = "SELECT MPA_ID,\n" +
                "       NAME\n" +
                "FROM MPA";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(QUERY);
        List<Mpa> mpaRatings = new ArrayList<>();
        while (mpaRows.next()) {
            mpaRatings.add(createMpaInMemory(mpaRows.getInt("MPA_ID"),
                    mpaRows.getString("NAME")));
        }
        return mpaRatings;
    }

    public Optional<Mpa> getById(Integer id) {
        final String QUERY = "SELECT NAME\n" +
                "FROM MPA\n" +
                "WHERE MPA_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(QUERY, id);
        if (mpaRows.next()) {
            return Optional.of(new Mpa(id,
                    mpaRows.getString("NAME")));
        } else {
            return Optional.empty();
        }
    }

    private Mpa createMpaInMemory(Integer id, String name) {
        return new Mpa(id, name);
    }

}