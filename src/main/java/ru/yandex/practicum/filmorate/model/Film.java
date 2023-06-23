package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @JsonIgnore
    private final Set<Integer> likes = new HashSet<>();
}
