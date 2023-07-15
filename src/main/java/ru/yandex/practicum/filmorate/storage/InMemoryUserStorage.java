package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final UserValidation userValidation = new UserValidation();
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private int currentId;

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }


    @Override
    public User create(User user) {
        userValidation.valid(user);
        int newId = getNewId();
        user.setId(newId);
        users.put(newId, user);
        log.info("Добавлен пользователь. id=" + newId);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            userValidation.valid(user);
            users.put(user.getId(), user);
            log.info("Изменен пользователь c id=" + user.getId());
        } else
            throw new NotFoundException("Пользователь с таким id не найден");
        return user;
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriends().put(friend.getId(), true);
        friend.getFriends().put(user.getId(), true);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    private int getNewId() {
        return ++currentId;
    }
}
