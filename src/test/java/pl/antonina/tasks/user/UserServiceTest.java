package pl.antonina.tasks.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        userService = new UserService(userRepository, bCryptPasswordEncoder, userMapper);
    }

    @Test
    void addUser() {
        final String email = "test@gmail.com";
        String password = "password";
        final UserType userType = UserType.CHILD;

        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);

        final String encodedPassword = "encodedPassword";
        when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);

        userService.addUser(userType, userData);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userCaptured = userArgumentCaptor.getValue();

        assertThat(userCaptured.getPassword()).isEqualTo(encodedPassword);
        assertThat(userCaptured.getEmail()).isEqualTo(email);
        assertThat(userCaptured.getType()).isEqualTo(userType);
    }

    @Test
    void addUserEmailExists() {
        String email = "test@gmail.com";
        User user = new User();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserType userType = UserType.PARENT;
        UserData userData = new UserData();
        userData.setEmail(email);
        assertThatThrownBy(() -> userService.addUser(userType, userData))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void updateUser() {
        final String email = "test@gmail.com";
        String password = "password";
        long userId = 123;
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        User user = new User();
        user.setId(userId);

        final String encodedPassword = "encodedPassword";
        when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);

        userService.updateUser(user, userData);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userCaptured = userArgumentCaptor.getValue();

        assertThat(userCaptured.getEmail()).isEqualTo(email);
        assertThat(userCaptured.getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void updateUserEmailExists() {
        String email = "Test@gmail.com";
        long userId = 123;
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmailAndIdNot(email, userId)).thenReturn(Optional.of(user));

        UserData userData = new UserData();
        userData.setEmail(email);
        user.setId(userId);
        assertThatThrownBy(() -> userService.updateUser(user, userData))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void getUser() {
        User user = new User();
        final UserView userView = new UserView();
        String email = "test@gmail.com";

        Principal userPrincipal = mock(Principal.class);
        when(userPrincipal.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.mapUserView(user)).thenReturn(userView);

        UserView userViewResult = userService.getUser(userPrincipal);

        assertThat(userViewResult).isEqualTo(userView);
    }
}