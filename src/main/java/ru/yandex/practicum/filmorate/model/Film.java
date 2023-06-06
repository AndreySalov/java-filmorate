package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Date;
import lombok.Data;
import lombok.NonNull;
import lombok.AllArgsConstructor;
@Data
public class Film {
    private int id;
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film(@NonNull String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
