package ru.yandex.practicum.filmorate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidation {
    private final Logger log = LoggerFactory.getLogger(UserValidation.class);

    public void valid(User user) throws ValidationException {
        if (!user.getEmail().contains("@")) {
            log.info("Ошибка при проверка пользователя. Поле E-mail некорректно");
            throw new ValidationException("Неверный адрес электронной почты");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Ошибка при проверка пользователя. Неверная дата рождения");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
