package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void start() {
        userService = new UserService(new UserDbStorage(new JdbcTemplate()));
    }

    @Test
    void testCreateAndGet() {
        User user1 = new User("andr", "", 1, "andr@yandex.ru", LocalDate.of(1979, 07, 29),null);
        userService.create(user1);
        User user2 = userService.getUser(1);
        assertEquals(user1, user2);
        assertThrows(NotFoundException.class, () -> userService.getUser(2));
    }
}
