package pl.antonina.tasks.user;

import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

public interface UserService {

    @Transactional
    User addUser(UserType userType, UserData userData);

    @Transactional
    User updateUser(User user, UserData userData);

    UserView getUser(Principal principal);
}