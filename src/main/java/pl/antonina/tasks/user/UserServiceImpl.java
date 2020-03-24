package pl.antonina.tasks.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public User addUser(UserType userType, UserData userData) {
        String email = userData.getEmail();
        boolean userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            throw new UserAlreadyExistsException("User with email=" + email + " already exist.");
        }
        User user = new User();
        user.setType(userType);
        user.setEmail(userData.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user, UserData userData) {
        String email = userData.getEmail();
        boolean userExists = userRepository.findByEmailAndIdNot(email, user.getId()).isPresent();
        if (userExists) {
            throw new UserAlreadyExistsException("User with email=" + email + " already exist.");
        }
        user.setEmail(userData.getEmail());
        if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public UserView getUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .map(userMapper::mapUserView)
                .orElseThrow(() -> new UserNotExistsException("User with email=" + principal.getName() + " doesn't exist."));
    }
}