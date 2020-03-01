package pl.antonina.tasks.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(UserData userData) {
        String email = userData.getEmail();
        boolean userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            throw new UserAlreadyExistsException("User with given email already exists");
        }
        User user = new User();
        mapUser(userData, user);
        return userRepository.save(user);
    }

    public User updateUser(User user, UserData userData) {
        String email = userData.getEmail();
        boolean userExists = userRepository.findByEmailAndIdNot(email, user.getId()).isPresent();
        if (userExists) {
            throw new UserAlreadyExistsException("User with given email already exists");
        }
        mapUser(userData, user);
        return userRepository.save(user);
    }

    private void mapUser(UserData userData, User user) {
        user.setEmail(userData.getEmail());
        user.setPassword(userData.getPassword());
    }
}