package ru.yandex.practicum.filmorate.model;

public class ValidationExeption  extends  RuntimeException{
    public ValidationExeption(String message) {
        super(message);
    }
}
