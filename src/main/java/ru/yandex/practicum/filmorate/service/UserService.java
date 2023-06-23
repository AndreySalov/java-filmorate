package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private final UserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private final UserValidation userValidation = new UserValidation();

    public List<User> getAllUsers() {
        List<User> usersList = inMemoryUserStorage.getAllUsers();
        return usersList;
    }

    public User getUser(int userId) {
        User user = inMemoryUserStorage.getUser(userId);
        if (user == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден.");
        return user;
    }

    public User create(User user) {
        userValidation.valid(user);
        return inMemoryUserStorage.create(user);
    }

    public User update(User user) {
        userValidation.valid(user);
        return inMemoryUserStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = getUser(userId);
        if (user.getFriends().contains(friendId)) {
            deleteFriend(userId, friendId);
        }
        User friend = getUser(friendId);
        inMemoryUserStorage.addFriend(user, friend);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        inMemoryUserStorage.deleteFriend(user, friend);
    }

    public List<User> getFriends(int userId) {
        final User user = getUser(userId);
        return user.getFriends().stream()
                .map(inMemoryUserStorage::getUser)
                .collect(Collectors.toList());
    }


    public List<User> getFriendsOtherUser(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        return user.getFriends().stream().filter(id -> friend.getFriends().contains(id))
                .map(inMemoryUserStorage::getUser)
                .collect(Collectors.toList());
    }
}
