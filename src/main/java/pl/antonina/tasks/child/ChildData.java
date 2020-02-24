package pl.antonina.tasks.child;

import lombok.Data;
import pl.antonina.tasks.user.Gender;

import java.time.Instant;

@Data
class ChildData {
    private String name;
    private Gender gender;
    private Instant birthDate;
}