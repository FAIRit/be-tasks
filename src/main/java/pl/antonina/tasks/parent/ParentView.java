package pl.antonina.tasks.parent;

import lombok.Data;
import pl.antonina.tasks.user.Gender;

@Data
public class ParentView {
    private String name;
    private String email;
    private Gender gender;
}