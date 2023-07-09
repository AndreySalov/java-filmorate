package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserValidation userValidation = new UserValidation();
    @Autowired
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage storage) {
        this.userStorage = storage;
    }

    public List<User> getAllUsers() {
        List<User> usersList = userStorage.getAllUsers();
        return usersList;
    }

    public User getUser(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден.");
        return user;
    }

    public User create(User user) {
        userValidation.valid(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        userValidation.valid(user);
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = getUser(userId);
        if (user.getFriends().containsKey(friendId)) {
            deleteFriend(userId, friendId);
        }
        User friend = getUser(friendId);
        userStorage.addFriend(user, friend, false);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        userStorage.deleteFriend(user, friend);
    }

    public List<User> getFriends(int userId) {
        final User user = getUser(userId);
        List<User> friends = new ArrayList<>();
        for (int friendId : user.getFriends().keySet()) {
            User friend = getUser(friendId);
            friends.add(friend);
        }
        return friends;
    }


    public List<User> getFriendsOtherUser(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        return user.getFriends().keySet().stream().filter(id -> friend.getFriends().containsKey(id))
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }
}
