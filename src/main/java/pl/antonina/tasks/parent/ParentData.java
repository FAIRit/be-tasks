package pl.antonina.tasks.parent;

import lombok.Data;
import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.UserData;

@Data
class ParentData {
    private String name;
    private Gender gender;
    private UserData userData;
}
