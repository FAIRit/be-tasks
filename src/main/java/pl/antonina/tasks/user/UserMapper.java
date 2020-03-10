package pl.antonina.tasks.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserView mapUserView(User user) {
        UserView userView = new UserView();
        userView.setType(user.getType());
        userView.setEmail(user.getEmail());
        return userView;
    }
}