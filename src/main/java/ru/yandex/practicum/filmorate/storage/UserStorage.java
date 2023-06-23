package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

public interface UserStorage {
//    public HashMap<Integer, User> getUsers();

    User getUser(int userId);

    List<User> getAllUsers();

    User create(User user);

    User updateUser(User user);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);

}
