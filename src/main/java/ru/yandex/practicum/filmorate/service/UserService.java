package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserStorage inMemoryUserStorage;

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
        return inMemoryUserStorage.create(user);
    }

    public User update(User user) {
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
        if (user.getFriends().size() == 0) {
            List<User> userFriendsEmpty = new ArrayList<>();
            return userFriendsEmpty;
        }
        List<User> userFriends = new ArrayList<>();
        for (int i : user.getFriends()) {
            userFriends.add(getUser(i));
        }
        return userFriends;
    }

    public List<User> getFriendsOtherUser(int userId, int otherId) {
        final User user = getUser(userId);
        List<Integer> userFriends = new ArrayList<>(user.getFriends());
        final User other = getUser(otherId);
        List<Integer> otherFriends = new ArrayList<>(other.getFriends());

        List<User> friendsOtherUser = new ArrayList<>();
        for (int i : userFriends)
            for (int j : otherFriends)
                if (i == j)
                    friendsOtherUser.add(getUser(i));
        return friendsOtherUser;

    }


}
