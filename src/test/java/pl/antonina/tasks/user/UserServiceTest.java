package pl.antonina.tasks.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        userService = new UserService(userRepository);
    }

    @Test
    void addUser() {
        String email = "test@gmail.com";
        String password = "password";

        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);

        userService.addUser(userData);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userCaptured = userArgumentCaptor.getValue();

        assertThat(userCaptured.getPassword()).isEqualTo(password);
        assertThat(userCaptured.getEmail()).isEqualTo(email);
    }

    @Test
    void addUserEmailExists() {
        String email = "test@gmail.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserData userData = new UserData();
        userData.setEmail(email);
        assertThatThrownBy(() -> userService.addUser(userData))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void updateUser() {
        String email = "test@gmail.com";
        String password = "password";
        long id = 123;
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);
        User user = new User();
        user.setId(id);

        userService.updateUser(user, userData);

        verify(userRepository).save(userArgumentCaptor.capture());
        User userCaptured = userArgumentCaptor.getValue();

        assertThat(userCaptured.getEmail()).isEqualTo(email);
        assertThat(userCaptured.getPassword()).isEqualTo(password);
    }

    @Test
    void updateUserEmailExists() {
        String email = "Test@gmail.com";
        long id = 123;

        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmailAndIdNot(email, id)).thenReturn(Optional.of(user));

        UserData userData = new UserData();
        userData.setEmail(email);
        user.setId(id);
        assertThatThrownBy(() -> userService.updateUser(user, userData))
                .isInstanceOf(UserAlreadyExistsException.class);
    }
}