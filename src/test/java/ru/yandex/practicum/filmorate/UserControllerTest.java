package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    UserValidation userValidation = new UserValidation();
    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void start() {
        UserService userService = new UserService(new UserDbStorage(new JdbcTemplate()));
        userController = new UserController(userService);
    }


    @Test
    void validateUserNameIsBlank() {
        User user = new User("andr", "", 1, "andr@yandex.ru", LocalDate.of(1979, 07, 29), null);
        userValidation.valid(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void validateUserNameIsNull() {
        User user = new User("andr", null, 1, "andr@yandex.ru", LocalDate.of(1979, 07, 29),null);
        userValidation.valid(user);
        assertEquals(user.getName(), user.getLogin());
    }
}