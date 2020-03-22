package pl.antonina.tasks.child;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.UserData;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
class ChildData {
    @NotEmpty
    @Length(min = 3)
    private String name;
    @NotNull
    private Gender gender;
    @NotNull
    private LocalDate birthDate;
    private UserData userData;
}