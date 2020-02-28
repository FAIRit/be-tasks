package pl.antonina.tasks.child;

import lombok.Data;
import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.UserData;

import java.time.Instant;
import java.time.LocalDate;

@Data
class ChildData {
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private UserData userData;
}