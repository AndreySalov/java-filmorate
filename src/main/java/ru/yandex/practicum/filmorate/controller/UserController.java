package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> usersList = userService.getAllUsers();
        log.debug("Количество пользователей :" + usersList.size());
        return usersList;
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        User savedUser = userService.create(user);
        log.debug("Добавлен пользователь :" + savedUser);
        return savedUser;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        User savedUser = userService.update(user);
        log.debug("Изменен пользователь :" + savedUser);
        return savedUser;
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getFriendsOtherUser(@PathVariable int userId, @PathVariable int otherId) {
        return userService.getFriendsOtherUser(userId, otherId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.deleteFriend(userId, friendId);
    }
}

