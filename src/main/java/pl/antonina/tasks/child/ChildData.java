package pl.antonina.tasks.child;

import lombok.Data;
import pl.antonina.tasks.user.Gender;

import java.time.Instant;
import java.time.LocalDate;

@Data
class ChildData {
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private String email;
    private String password;
}