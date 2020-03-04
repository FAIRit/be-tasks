package pl.antonina.tasks.parent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.user.*;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
    @Mock
    private LoggedUserService loggedUserService;

    private ParentService parentService;

    @Captor
    private ArgumentCaptor<Parent> parentArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        parentService = new ParentService(parentRepository, parentMapper, userRepository, userService, loggedUserService);
    }

    @Test
    void getParent() {
        Parent parent = new Parent();
        ParentView parentView = new ParentView();

        Principal principal = mock(Principal.class);
        when(loggedUserService.getParent(principal)).thenReturn(parent);
        when(parentMapper.mapParentView(parent)).thenReturn(parentView);

        ParentView parentViewResult = parentService.getParent(principal);

        assertThat(parentViewResult).isEqualTo(parentView);
    }

    @Test
    void addParent() {
        Gender gender = Gender.FEMALE;
        String name = "Antonina";
        UserType userType = UserType.PARENT;
        ParentData parentData = new ParentData();
        parentData.setGender(gender);
        parentData.setName(name);

        User user = new User();
        user.setType(userType);
        UserData userData = new UserData();
        parentData.setUserData(userData);
        when(userService.addUser(userType, userData)).thenReturn(user);

        parentService.addParent(parentData);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
        assertThat(parentCaptured.getUser().getType()).isEqualTo(userType);
    }

    @Test
    void updateParent() {
        String name = "Michał";
        Gender gender = Gender.MALE;
        ParentData parentData = new ParentData();
        parentData.setName(name);
        parentData.setGender(gender);

        Parent parent = new Parent();
        User user = new User();
        parent.setUser(user);
        UserData userData = new UserData();
        parentData.setUserData(userData);
        Principal principal = mock(Principal.class);
        when(loggedUserService.getParent(principal)).thenReturn(parent);
        when(userService.updateUser(user, userData)).thenReturn(user);

        parentService.updateParent(parentData, principal);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
    }

    @Test
    void deleteById() {
        long parentId = 123;
        long userId = 987;
        Parent parent = new Parent();
        User user = new User();
        user.setId(userId);
        parent.setUser(user);
        parent.setId(parentId);

        Principal principal = mock(Principal.class);
        when(loggedUserService.getParent(principal)).thenReturn(parent);

        parentService.deleteParent(principal);

        verify(userRepository).deleteById(userId);
        verify(parentRepository).deleteById(parentId);
    }
}