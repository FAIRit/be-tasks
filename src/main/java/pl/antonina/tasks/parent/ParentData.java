package pl.antonina.tasks.parent;

import lombok.Data;
import pl.antonina.tasks.user.Gender;

@Data
class ParentData {
    private String name;
    private Gender gender;
}
