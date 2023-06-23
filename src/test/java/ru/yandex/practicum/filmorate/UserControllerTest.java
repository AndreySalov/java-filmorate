package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserValidation userValidation = new UserValidation();

    @Test
    void validateUserLogin() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User(1, "srg@yandex.ru", "test_login", "test_name", LocalDate.of(1987, 10, 10), new HashSet<>());
        assertThrows(ValidationException.class, () -> userValidation.valid(user));
    }

    @Test
    void validateUserNameIsBlank() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User(1, "srg@yandex.ru", "test_login", "test_name", LocalDate.of(1987, 10, 10), new HashSet<>());
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void validateUserNameIsNull() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User(1, "srg@yandex.ru", "test_login", null, LocalDate.of(1987, 10, 10), new HashSet<>());
        assertEquals(user.getName(), user.getLogin());
    }


}
