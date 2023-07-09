package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Qualifier("FilmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilm(Integer filmId) {

        // Сформируем запрос выборки фильмов.
        final String FILM_QUERY = "SELECT FILMS.FILM_ID,\n" +
                "       FILMS.NAME,\n" +
                "       FILMS.DESCRIPTION,\n" +
                "       FILMS.RELEASE_DATE,\n" +
                "       FILMS.DURATION,\n" +
                "       FILMS.MPA_ID,\n" +
                "       MPA.NAME as MPA_NAME\n" +
                "FROM FILMS\n" +
                "LEFT JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID\n" +
                "WHERE FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(FILM_QUERY, filmId);
        // Сформируем запрос выборки жанров.
        final String GENRE_QUERY = "SELECT FILM_GENRES.FILM_ID,\n" +
                "       FILM_GENRES.GENRE_ID,\n" +
                "       GENRE.NAME\n" +
                "FROM FILM_GENRES\n" +
                "LEFT JOIN GENRE ON FILM_GENRES.GENRE_ID = GENRE.GENRE_ID\n" +
                "WHERE FILM_GENRES.FILM_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(GENRE_QUERY, filmId);
        // Сформируем запрос выборки лайков.
        final String LIKE_QUERY = "SELECT FILM_ID,\n" +
                "       USER_ID\n" +
                "FROM LIKES\n" +
                "WHERE FILM_ID = ?";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(LIKE_QUERY, filmId);

        // Сформируем фильмы по результатам запросов.
        List<Film> createdFilms = createFilmsFromRowSets(filmRows, genreRows, likeRows);
        if (createdFilms.isEmpty())
            throw new NotFoundException("Фильм не найден.");

        return createdFilms.get(0);
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).
                withTableName("FILMS").
                usingGeneratedKeyColumns("FILM_ID").
                usingColumns("NAME",
                        "DESCRIPTION",
                        "RELEASE_DATE",
                        "DURATION",
                        "MPA_ID");
        Map<String, Object> params = new HashMap<>();
        params.put("NAME", film.getName());
        params.put("DESCRIPTION", film.getDescription());
        params.put("RELEASE_DATE", film.getReleaseDate());
        params.put("DURATION", film.getDuration());
        if (film.getMpa() != null) {
            params.put("MPA_ID", film.getMpa().getId());
        }
        Integer filmId = simpleJdbcInsert.executeAndReturnKey(params).intValue();
        film.setId(filmId);
        addFilmGenres(film);
        return getFilm(filmId);
    }

    private void addFilmGenres(Film film) {
        if (film.getGenres() != null) {
            final String SQL_QUERY = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(SQL_QUERY, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        Integer filmId = film.getId();
        final String FILM_UPDATE = "UPDATE FILMS SET NAME = ?," +
                "   DESCRIPTION = ?," +
                "   RELEASE_DATE = ?," +
                "   DURATION = ?," +
                "   MPA_ID = ?\n" +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(FILM_UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId(),
                filmId);
        deleteFilmGenres(filmId);
        addFilmGenres(film);
        deleteFilmLikes(filmId);
        addFilmLikes(film);

        return getFilm(filmId);
    }

    private void deleteFilmGenres(Integer filmId) {
        final String FILM_GENRE_DELETE = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(FILM_GENRE_DELETE, filmId);
    }

    private void deleteFilmLikes(Integer filmId) {
        final String FILM_LIKES_DELETE = "DELETE FROM LIKES WHERE FILM_ID = ?";
        jdbcTemplate.update(FILM_LIKES_DELETE, filmId);
    }

    private void addFilmLikes(Film film) {
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addFilmLikes(film.getId(), userId);
            }
        }
    }

    @Override
    public List<Film> getAllFilms() {
        final String FILM_QUERY = "SELECT FILMS.FILM_ID,\n" +
                "       FILMS.NAME,\n" +
                "       FILMS.DESCRIPTION,\n" +
                "       FILMS.RELEASE_DATE,\n" +
                "       FILMS.DURATION,\n" +
                "       FILMS.MPA_ID,\n" +
                "       MPA.NAME as MPA_NAME\n" +
                "FROM FILMS\n" +
                "LEFT JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(FILM_QUERY);
        final String GENRE_QUERY = "SELECT FILM_GENRES.FILM_ID,\n" +
                "       FILM_GENRES.GENRE_ID,\n" +
                "       GENRE.NAME\n" +
                "FROM FILM_GENRES\n" +
                "LEFT JOIN GENRE ON FILM_GENRES.GENRE_ID = GENRE.GENRE_ID";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(GENRE_QUERY);
        final String LIKE_QUERY = "SELECT FILM_ID,\n" +
                "       USER_ID\n" +
                "FROM LIKES";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(LIKE_QUERY);
        return createFilmsFromRowSets(filmRows, genreRows, likeRows);
    }

    @Override
    public void addLike(Film film, User user) {
        addFilmLikes(film.getId(), user.getId());
    }

    private void addFilmLikes(Integer filmId, Integer userId) {
        final String SQL_QUERY = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(SQL_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Film film, User user) {
        final String FILM_LIKES_DELETE = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(FILM_LIKES_DELETE, film.getId(), user.getId());
    }

    public List<Film> getPopular(Integer count) {
        if (count <= 0) {
            return new ArrayList<>();
        }

        final String SQL_QUERY = "SELECT FILMS.FILM_ID\n" +
                "FROM FILMS\n" +
                "LEFT JOIN LIKES ON FILMS.FILM_ID = LIKES.FILM_ID\n" +
                "GROUP BY FILMS.FILM_ID\n" +
                "ORDER BY COUNT(LIKES.USER_ID) DESC\n" +
                "LIMIT " + count;
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(SQL_QUERY);
        // Обработаем запрос.
        List<Film> films = new ArrayList<>();
        while (filmsRows.next()) {
            Integer filmId = filmsRows.getInt("FILM_ID");
            Film film = getFilm(filmId);
            films.add(film);
        }
        return films;
    }

    private List<Film> createFilmsFromRowSets(SqlRowSet filmRows,
                                              SqlRowSet genreRows,
                                              SqlRowSet likeRows) {
        // Получим жанры фильмов.
        Map<Integer, TreeSet<Genre>> filmsGenre = createFilmsGenreFromRowSet(genreRows);
        // Получим лайки фильмов.
        Map<Integer, Set<Integer>> filmsLikes = createFilmsLikesFromRowSet(likeRows);
        // Создадим и заполним объекты фильмов.
        List<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            // Сформируем объект mpa.
            int mpaId = filmRows.getInt("MPA_ID");
            Mpa filmMpa = null;
            if (mpaId > 0) {
                filmMpa = new Mpa(filmRows.getInt("MPA_ID"),
                        filmRows.getString("MPA_NAME"));
            }
            // Сформируем объект фильма.
            Integer filmId = filmRows.getInt("FILM_ID");
            Film film = new Film(filmId,
                    filmRows.getString("NAME"),
                    filmRows.getString("DESCRIPTION"),
                    Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate(),
                    filmRows.getInt("DURATION"),
                    new HashSet<>(),
                    filmMpa,
                    filmsGenre.getOrDefault(filmId, new TreeSet<>()));

            if (filmsLikes.containsKey(filmId)) {
                film.setLikes(filmsLikes.get(filmId));
            }
            films.add(film);
        }
        return films;
    }

    private Map<Integer, TreeSet<Genre>> createFilmsGenreFromRowSet(SqlRowSet genreRows) {
        Map<Integer, TreeSet<Genre>> filmsGenre = new HashMap<>();
        while (genreRows.next()) {
            // Сформируем объект жанра.
            Genre filmGenre = new Genre(genreRows.getInt("GENRE_ID"),
                    genreRows.getString("NAME"));
            // Получим идентификатор фильма который относится к текущему жанру.
            Integer filmId = genreRows.getInt("FILM_ID");

            // Добавим полученный жанр в Set жанров фильма.
            if (!filmsGenre.containsKey(filmId)) {
                filmsGenre.put(filmId, new TreeSet<>());
            }
            Set<Genre> genres = filmsGenre.get(filmId);
            genres.add(filmGenre);
        }

        return filmsGenre;
    }

    private Map<Integer, Set<Integer>> createFilmsLikesFromRowSet(SqlRowSet likeRows) {
        Map<Integer, Set<Integer>> filmsLikes = new HashMap<>();
        while (likeRows.next()) {
            Integer filmId = likeRows.getInt("FILM_ID");
            Integer userId = likeRows.getInt("USER_ID");
            if (!filmsLikes.containsKey(filmId)) {
                filmsLikes.put(filmId, new HashSet<>());
            }
            Set<Integer> likes = filmsLikes.get(filmId);
            likes.add(userId);
        }

        return filmsLikes;
    }
}
