package pl.antonina.tasks.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserData {
    @Email
    @NotNull
    private String email;
    private String password;
}
