package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Map;

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Логин не может содержать проблеы и спец. символы")
    private String login;
    private String name;
    private int id;
    @NotEmpty
    private String email;
    @NonNull
    private LocalDate birthday;
    @JsonIgnore
    private Map<Integer, Boolean> friends;
}
