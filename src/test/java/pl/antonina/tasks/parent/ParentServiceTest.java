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
        parentService = new ParentServiceImpl(parentRepository, parentMapper, userRepository, userService, loggedUserService);
    }

    @Test
    void getParent() {
        Parent parent = new Parent();
        final ParentView parentView = new ParentView();

        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(parentMapper.mapParentView(parent)).thenReturn(parentView);

        ParentView parentViewResult = parentService.getParent(parentPrincipal);

        assertThat(parentViewResult).isEqualTo(parentView);
    }

    @Test
    void addParent() {
        final Gender gender = Gender.FEMALE;
        final String name = "Antonina";
        final UserType userType = UserType.PARENT;
        ParentData parentData = new ParentData();
        parentData.setGender(gender);
        parentData.setName(name);
        User user = new User();
        user.setType(userType);
        UserData userData = new UserData();
        parentData.setUserData(userData);
        when(userService.addUser(userType, userData)).thenReturn(user);
        Parent parent = new Parent();
        parent.setId(456L);
        when(parentRepository.save(any())).thenReturn(parent);

        parentService.addParent(parentData);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
        assertThat(parentCaptured.getUser().getType()).isEqualTo(userType);
    }

    @Test
    void updateParent() {
        final String name = "Michał";
        final Gender gender = Gender.MALE;
        ParentData parentData = new ParentData();
        parentData.setName(name);
        parentData.setGender(gender);

        Parent parent = new Parent();
        User user = new User();
        parent.setUser(user);
        UserData userData = new UserData();
        parentData.setUserData(userData);
        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(userService.updateUser(user, userData)).thenReturn(user);

        parentService.updateParent(parentData, parentPrincipal);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
    }

    @Test
    void deleteById() {
        final long parentId = 123;
        final long userId = 987;
        Parent parent = new Parent();
        User user = new User();
        user.setId(userId);
        parent.setUser(user);
        parent.setId(parentId);

        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);

        parentService.deleteParent(parentPrincipal);

        verify(userRepository).deleteById(userId);
        verify(parentRepository).deleteById(parentId);
    }
}