package pl.antonina.tasks.parent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.user.*;

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
    @Mock
    private UserService userService;

    private ParentService parentService;

    @Captor
    private ArgumentCaptor<Parent> parentArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        parentService = new ParentService(parentRepository, parentMapper, userRepository, userService);
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
        Gender gender = Gender.FEMALE;
        String name = "Antonina";
        ParentData parentData = new ParentData();
        parentData.setGender(gender);
        parentData.setName(name);

        User user = new User();
        UserData userData = new UserData();
        parentData.setUserData(userData);
        when(userService.addUser(userData)).thenReturn(user);

        parentService.addParent(parentData);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
    }

    @Test
    void updateParent() {
        long id = 123;
        ParentData parentData = new ParentData();
        String name = "Michał";
        Gender gender = Gender.MALE;
        parentData.setName(name);
        parentData.setGender(gender);

        Parent parent = new Parent();
        User user = new User();
        parent.setUser(user);
        UserData userData = new UserData();
        parentData.setUserData(userData);
        when(parentRepository.findById(id)).thenReturn(Optional.of(parent));

        when(userService.updateUser(user, userData)).thenReturn(user);

        parentService.updateParent(id, parentData);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
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