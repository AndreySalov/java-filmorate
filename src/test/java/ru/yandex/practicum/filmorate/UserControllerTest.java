package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserController;
import ru.yandex.practicum.filmorate.model.ValidationExeption;

import static org.junit.jupiter.api.Assertions.*;



import java.time.LocalDate;

public class UserControllerTest {
    private UserController userController;
    @BeforeEach
    public void start(){
        userController = new UserController();
    }
    @Test
    void createValidUser(){
        User newUser = userController.create(new User("test@test", "login" , "name" , LocalDate.now()));
        assertNotNull(newUser.getId());
    }
    @Test
    void createNotValidUser(){
        assertThrows(ValidationExeption.class,()->userController.create(new User("test", "login" , "name" , LocalDate.now())));
        assertThrows(ValidationExeption.class,()->userController.create(new User("test@test", "login space" , "name" , LocalDate.now())));
        assertThrows(ValidationExeption.class,()->userController.create(new User("test@test", "" , "name" , LocalDate.now())));
        assertThrows(ValidationExeption.class,()->userController.create(new User("test@test", "login" , "name" , LocalDate.now().plusDays(1))));
    }
}
