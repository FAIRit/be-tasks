package pl.antonina.tasks.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
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

    @Transactional
    public User updateUser(User user, UserData userData) {
        String email = userData.getEmail();
        boolean userExists = userRepository.findByEmailAndIdNot(email, user.getId()).isPresent();
        if (userExists) {
            throw new UserAlreadyExistsException("User with given email already exists.");
        }
        user.setEmail(userData.getEmail());
        if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        }
        return userRepository.save(user);
    }

    UserView getUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .map(userMapper::mapUserView)
                .orElseThrow(() -> new UserNotExistsException("User with given email doesn't exist."));
    }
}