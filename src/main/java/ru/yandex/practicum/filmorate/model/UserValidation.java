package ru.yandex.practicum.filmorate.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class UserValidation {
    private final static Logger log = LoggerFactory.getLogger(UserValidation.class);
    public void valid(User user) throws ValidationExeption{
        if (user.getLogin().length()==0){
            log.info("Ошибка при проверка пользователя. Поле логин пустое");
            throw new ValidationExeption("Логин не может быть пустым.");
        } else if (user.getLogin().indexOf(" ")>=0) {
            log.info("Ошибка при проверка пользователя. Поле логин содержит пробелы");
            throw new ValidationExeption("Логин не может содержать пробелы");
        }else if (user.getEmail().length()==0 || user.getEmail().indexOf("@")<0)
        {
            log.info("Ошибка при проверка пользователя. Поле E-mail некорректно");
            throw new ValidationExeption("Неверный адрес электронной почты");
        }
        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null ||  user.getName().length()==0){
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())){
            log.info("Ошибка при проверка пользователя. Неверная дата рождения");
            throw new ValidationExeption("Дата рождения не может быть в будушем.");
        }
    }
}
