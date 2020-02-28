package pl.antonina.tasks.parent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.User;
import pl.antonina.tasks.user.UserData;
import pl.antonina.tasks.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParentServiceTest {

    @Mock
    private ParentMapper parentMapper;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private UserRepository userRepository;

    private ParentService parentService;

    @Captor
    private ArgumentCaptor<Parent> parentArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        parentService = new ParentService(parentRepository, parentMapper, userRepository);
    }

    @Test
    void getParent() {
        long id = 123;

        Parent parent = new Parent();
        ParentView parentView = new ParentView();

        when(parentRepository.findById(id)).thenReturn(Optional.of(parent));
        when(parentMapper.mapParentView(parent)).thenReturn(parentView);

        ParentView parentViewResult = parentService.getParent(id);
        assertThat(parentViewResult).isEqualTo(parentView);
    }

    @Test
    void addParent() {
        String password = "password";
        String email = "amarikhina@gmail.com";
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);

        Gender gender = Gender.FEMALE;
        String name = "Antonina";
        ParentData parentData = new ParentData();
        parentData.setGender(gender);
        parentData.setName(name);
        parentData.setUserData(userData);

        parentService.addParent(parentData);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
        assertThat(parentCaptured.getUser().getPassword()).isEqualTo(password);
        assertThat(parentCaptured.getUser().getEmail()).isEqualTo(email);
    }

    @Test
    void addParentEmailExists() {
        String email = "test@gmail.com";
        UserData userData = new UserData();
        userData.setEmail(email);
        ParentData parentData = new ParentData();
        parentData.setUserData(userData);

        User userExists = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userExists));

        assertThatThrownBy(() -> parentService.addParent(parentData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with given email already exists");
    }

    @Test
    void updateParent() {
        long id = 123;
        String password = "password";
        String email = "amarikhina@gmail.com";
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setPassword(password);

        ParentData parentData = new ParentData();
        String name = "Michał";
        Gender gender = Gender.MALE;
        parentData.setName(name);
        parentData.setGender(gender);
        parentData.setUserData(userData);

        Parent parent = new Parent();
        parent.setUser(new User());
        when(parentRepository.findById(id)).thenReturn(Optional.of(parent));

        parentService.updateParent(id, parentData);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
        assertThat(parentCaptured.getUser().getEmail()).isEqualTo(email);
        assertThat(parentCaptured.getUser().getPassword()).isEqualTo(password);
    }

    @Test
    void updateParentEmailExists() {
        String email = "test@gmail.com";
        UserData userData = new UserData();
        userData.setEmail(email);
        ParentData parentData = new ParentData();
        parentData.setUserData(userData);

        User existingUser = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> parentService.updateParent(123, parentData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with given email already exists");
    }

    @Test
    void deleteById() {
        long id = 123;
        Parent parent = new Parent();
        User user = new User();
        user.setId(987L);
        parent.setUser(user);
        when(parentRepository.findById(id)).thenReturn(Optional.of(parent));

        parentService.deleteParent(id);

        verify(userRepository).deleteById(user.getId());
        verify(parentRepository).deleteById(id);
    }
}