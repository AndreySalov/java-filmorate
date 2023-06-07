package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;
import java.util.Set;

public class UserControllerTest {
    private UserController userController;
    private Validator validator;

    @BeforeEach
    public void start() {
        userController = new UserController();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    void createValidUser() {
        User newUser = userController.create(new User(0, "test@test", "login", "name", LocalDate.now()));
        assertNotNull(newUser.getId());
    }

    @Test
    void createNotValidUser() {
        assertThrows(ValidationException.class, () -> userController.create(new User(0, "test", "login", "name", LocalDate.now())));
        assertThrows(ValidationException.class, () -> userController.create(new User(0, "test@test", "login", "name", LocalDate.now().plusDays(1))));
        User newUser = new User(0, "test", "login", "name", LocalDate.now());
        newUser = new User(0, "test@test", "", "name", LocalDate.now());
        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        assertFalse(violations.isEmpty());
        newUser = new User(0, "test@test", "а а", "name", LocalDate.now());
        violations = validator.validate(newUser);
        assertFalse(violations.isEmpty());

    }
}
