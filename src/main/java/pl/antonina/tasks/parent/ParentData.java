package pl.antonina.tasks.parent;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.UserData;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
class ParentData {
    @NotEmpty
    @Length(min = 3)
    private String name;
    @NotNull
    private Gender gender;
    private UserData userData;
}
