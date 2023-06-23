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

class UserControllerTest {
    UserValidation userValidation = new UserValidation();

    @Test
    void validateUserLogin() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User("andr", "Andrey", 1, "andr@yandex.ru", LocalDate.of(1979, 07, 29), new HashSet<>());
        assertThrows(ValidationException.class, () -> userValidation.valid(user));
    }

    @Test
    void validateUserNameIsBlank() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User("andr", "", 1, "andr@yandex.ru", LocalDate.of(1979, 07, 29), new HashSet<>());
        userValidation.valid(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void validateUserNameIsNull() {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);
        User user = new User("andr", null, 1, "andr@yandex.ru", LocalDate.of(1979, 07, 29), new HashSet<>());
        userValidation.valid(user);
        assertEquals(user.getName(), user.getLogin());
    }
}