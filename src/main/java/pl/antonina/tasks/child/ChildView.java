package pl.antonina.tasks.child;

import lombok.Data;
import pl.antonina.tasks.user.Gender;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class ChildView {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private LocalDate birthDate;
    private Integer points;
}