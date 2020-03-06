package pl.antonina.tasks.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User addUser(UserType userType, UserData userData) {
        String email = userData.getEmail();
        boolean userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            throw new UserAlreadyExistsException("User with given email already exists.");
        }
        User user = new User();
        user.setType(userType);
        user.setEmail(userData.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user, UserData userData) {
        String email = userData.getEmail();
        boolean userExists = userRepository.findByEmailAndIdNot(email, user.getId()).isPresent();
        if (userExists) {
            throw new UserAlreadyExistsException("User with given email already exists.");
        }
        user.setEmail(userData.getEmail());
        if (userData.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        }
        return userRepository.save(user);
    }
}