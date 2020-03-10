package pl.antonina.tasks.user;

import lombok.Data;

@Data
public class UserView {
    private String email;
    private UserType type;
}