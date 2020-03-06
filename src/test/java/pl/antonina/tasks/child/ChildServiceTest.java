package pl.antonina.tasks.child;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.user.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {

    @Mock
    private ChildRepository childRepository;
    @Mock
    private ChildMapper childMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private LoggedUserService loggedUserService;

    private ChildService childService;

    @Captor
    private ArgumentCaptor<Child> childArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        childService = new ChildService(childRepository, childMapper, userRepository, userService, loggedUserService);
    }

    @Test
    void getChild() {
        final long childId = 123;

        Child child = new Child();
        final ChildView childView = new ChildView();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        when(childMapper.mapChildView(child)).thenReturn(childView);

        ChildView childViewResult = childService.getChild(childId);
        assertThat(childViewResult).isEqualTo(childView);
    }

    @Test
    void getChildrenByParentId() {
        Child child1 = new Child();
        Child child2 = new Child();
        List<Child> childList = List.of(child1, child2);
        ChildView childView1 = new ChildView();
        ChildView childView2 = new ChildView();
        final List<ChildView> childViewList = List.of(childView1, childView2);

        Principal parentPrincipal = mock(Principal.class);
        Parent parent = new Parent();
        long parentId = 123;
        parent.setId(parentId);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(childRepository.findByParentId(parentId)).thenReturn(childList);
        when(childMapper.mapChildView(child1)).thenReturn(childView2);
        when(childMapper.mapChildView(child2)).thenReturn(childView2);

        List<ChildView> childViewListResult = childService.getChildrenByParent(parentPrincipal);

        assertThat(childViewListResult).isEqualTo(childViewList);
    }

    @Test
    void addChild() {
        final String name = "Natalia";
        final LocalDate birthDate = LocalDate.of(2017, 2, 16);
        final Gender gender = Gender.FEMALE;
        final UserType userType = UserType.CHILD;

        ChildData childData = new ChildData();
        childData.setName(name);
        childData.setBirthDate(birthDate);
        childData.setGender(gender);
        UserData userData = new UserData();
        childData.setUserData(userData);

        final Parent parent = new Parent();
        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        final User user = new User();
        user.setType(userType);
        when(userService.addUser(userType, userData)).thenReturn(user);

        childService.addChild(parentPrincipal, childData);

        verify(childRepository).save(childArgumentCaptor.capture());
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(childCaptured.getName()).isEqualTo(name);
        assertThat(childCaptured.getBirthDate()).isEqualTo(birthDate);
        assertThat(childCaptured.getGender()).isEqualTo(gender);
        assertThat(childCaptured.getParent()).isEqualTo(parent);
        assertThat(childCaptured.getPoints()).isZero();
        assertThat(childCaptured.getUser()).isEqualTo(user);
        assertThat(childCaptured.getUser().getType()).isEqualTo(userType);
    }

    @Test
    void updateChild() {
        long childId = 123;
        final Gender gender = Gender.FEMALE;
        final LocalDate birthDate = LocalDate.of(2017, 2, 16);
        final String name = "Natalia";
        UserData userData = new UserData();

        ChildData childData = new ChildData();
        childData.setGender(gender);
        childData.setBirthDate(birthDate);
        childData.setName(name);
        childData.setUserData(userData);

        Child child = new Child();
        User user = new User();
        child.setUser(user);
        when(userService.updateUser(user, userData)).thenReturn(user);

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));

        childService.updateChild(childId, childData);

        verify(childRepository).save(childArgumentCaptor.capture());
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(childCaptured.getGender()).isEqualTo(gender);
        assertThat(childCaptured.getBirthDate()).isEqualTo(birthDate);
        assertThat(childCaptured.getName()).isEqualTo(name);
    }

    @Test
    void deleteChild() {
        final long childId = 123;

        Child child = new Child();
        final User user = new User();
        final long userId = 987;
        user.setId(userId);
        child.setUser(user);
        when(childRepository.findById(childId)).thenReturn(Optional.of(child));

        childService.deleteChild(childId);

        verify(userRepository).deleteById(user.getId());
        verify(childRepository).deleteById(childId);
    }
}