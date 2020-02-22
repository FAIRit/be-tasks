package pl.antonina.tasks.parent;

import lombok.Data;
import pl.antonina.tasks.gender.Gender;

@Data
public class ParentData {
    private String name;
    private Gender gender;
}
