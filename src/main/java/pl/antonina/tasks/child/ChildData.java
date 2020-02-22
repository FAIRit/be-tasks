package pl.antonina.tasks.child;

import lombok.Data;
import pl.antonina.tasks.gender.Gender;

import java.time.Instant;

@Data
public class ChildData {
    private String name;
    private Gender gender;
    private Instant birthDate;
}