package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.*;

@Component
@Qualifier("UserDbStorage")
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserValidation userValidation = new UserValidation();

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(int userId) {
        String sqlQuery =
                "SELECT u.user_id, u.email, u.login, u.name, u.birthday , f.incoming_user_id , f.status " +
                        "FROM users u LEFT JOIN FRIENDS f ON u.user_id = f.outgoing_user_id " +
                        "WHERE u.user_id=?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        // Получим пользователей запроса.
        List<User> users = createUsersFromRowSet(userRows);
        if (users.isEmpty())
            throw new NotFoundException("Пользователь не найден.");
        return users.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery =
                "SELECT u.user_id, u.email, u.login, u.name, u.birthday , f.incoming_user_id , f.status " +
                        "FROM users u LEFT JOIN FRIENDS f ON u.user_id = f.outgoing_user_id ORDER BY u.user_id";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery);
        // Сформируем пользователей по результатам запроса.
        return createUsersFromRowSet(userRows);
    }

    @Override
    public User create(User user) {
        userValidation.valid(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(toMap(user)).intValue());
        user.setFriends(new HashMap<>());
        return user;
    }

    @Override
    public User updateUser(User user) {
        userValidation.valid(user);
        String sqlQuery = "UPDATE users " +
                "SET email=?, login=?, name=?, birthday=? " +
                "WHERE user_id=?";

        int rowsCount = jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());

        if (rowsCount > 0) {
            return user;
        }
        throw new NotFoundException("Пользователь не найден.");
    }

    public Map<Integer, Boolean> getFriends(User user) {
        // Сформируем запрос выборки друзей пользователя.
        final String SQL_QUERY = "SELECT INCOMING_USER_ID,\n" +
                "       STATUS\n" +
                "FROM FRIENDS\n" +
                "WHERE OUTGOING_USER_ID = ?";
        // Обработаем запрос.
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet(SQL_QUERY, user.getId());
        Map<Integer, Boolean> friends = new HashMap<>();
        while (friendRows.next()) {
            Integer friendId = friendRows.getInt("INCOMING_USER_ID");
            Boolean status = friendRows.getBoolean("STATUS");
            friends.put(friendId, status);
        }
        return friends;
    }

    public List<User> getFriendsOtherUser(Integer friendId1, Integer friendId2) {
        // Сформируем запрос получения идентификаторов общих друзей.
        final String SQL_QUERY = "SELECT FRIENDS.INCOMING_USER_ID " +
                "FROM FRIENDS F1" +
                "JOIN (SELECT INCOMING_USER_ID FROM FRIENDS WHERE FRIENDS.OUTGOING_USER_ID = ? ) F2 " +
                "ON F1.INCOMING_USER_ID = F2.INCOMING_USER_ID " +
                "WHERE OUTGOING_USER_ID = ?";

        // Обработаем результат запроса.
        SqlRowSet mutualFriendsRows = jdbcTemplate.queryForRowSet(SQL_QUERY, friendId1, friendId2);
        List<User> friends = new ArrayList<>();
        while (mutualFriendsRows.next()) {
            Integer friendId = mutualFriendsRows.getInt("INCOMING_USER_ID");
            Optional<User> friend = Optional.ofNullable(getUser(friendId));
            friend.ifPresent(friends::add);
        }

        return friends;
    }

    @Override
    public void addFriend(User user, User friend, boolean status) {
        getUser(user.getId());
        getUser(friend.getId());
        String sqlQuery =
                "INSERT " +
                        "INTO friends (outgoing_user_id,incoming_user_id,  status) " +
                        "VALUES(?, ? , ?)";
        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId(), String.valueOf(status));
    }

    @Override
    public void deleteFriend(User user, User friend) {
        String sqlQuery =
                "DELETE " +
                        "FROM friends " +
                        "WHERE OUTGOING_USER_ID = ? AND INCOMING_USER_ID = ?";

        jdbcTemplate.update(sqlQuery, user.getId(), friend.getId());
    }


    private List<User> createUsersFromRowSet(SqlRowSet userRows) {
        // обрабатываем результат выполнения запроса
        List<User> users = new ArrayList<>();
        int userId = 0;
        while (userRows.next()) {
            int currentUserId = userRows.getInt("user_id");
            if (userId != currentUserId) {
                userId = currentUserId;
                // Добавим нового пользователя в список.
                users.add(new User(userRows.getString("LOGIN"),
                        userRows.getString("NAME"),
                        userRows.getInt("user_id"),
                        userRows.getString("email"),
                        userRows.getDate("birthday").toLocalDate(),
                        new HashMap<>()));
            }
            int friendId = userRows.getInt("incoming_user_id");
            if (friendId != 0) {
                // Есть данные о друге. Добавим их.
                Map<Integer, Boolean> friends = users.get(users.size() - 1).getFriends();
                friends.put(friendId, Boolean.valueOf(userRows.getString("status")));
            }
        }
        return users;
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }
}
