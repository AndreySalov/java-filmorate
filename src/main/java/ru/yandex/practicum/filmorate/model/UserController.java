package ru.yandex.practicum.filmorate.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final UserValidation userValidation = new UserValidation();
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private int currentId;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        userValidation.valid(user);
        int newId = getNewId();
        user.setId(newId);
        users.put(newId, user);
        log.info("Добавлен пользователь. id=" + newId);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            userValidation.valid(user);
            users.put(user.getId(), user);
            log.info("Изменен пользователь c id=" + user.getId());
        } else
            throw new ValidationExeption("Пользователь с таким id не найден");
        return user;
    }

    private int getNewId() {
        return ++currentId;
    }
}
