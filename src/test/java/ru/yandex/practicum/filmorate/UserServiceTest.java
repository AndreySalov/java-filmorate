package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void start() {
        userService = new UserService();
    }

    @Test
    void testCreateAndGet() {
        User user1 = new User("andr", "", 1, "andr@yandex.ru", LocalDate.of(1979, 07, 29));
        userService.create(user1);
        User user2 = userService.getUser(1);
        assertEquals(user1, user2);
        assertThrows(NotFoundException.class, () -> userService.getUser(2));
    }
}
