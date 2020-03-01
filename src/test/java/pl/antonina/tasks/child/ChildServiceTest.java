package pl.antonina.tasks.child;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.user.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {

    @Mock
    private ChildRepository childRepository;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private ChildMapper childMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;


    private ChildService childService;

    @Captor
    private ArgumentCaptor<Child> childArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        childService = new ChildService(childRepository, parentRepository, childMapper, userRepository, userService);
    }

    @Test
    void getChild() {
        long id = 123;

        Child child = new Child();
        ChildView childView = new ChildView();

        when(childRepository.findById(id)).thenReturn(Optional.of(child));
        when(childMapper.mapChildView(child)).thenReturn(childView);

        ChildView childViewResult = childService.getChild(id);
        assertThat(childViewResult).isEqualTo(childView);
    }

    @Test
    void getChildrenByParentId() {
        long parentId = 123;
        Child child = new Child();
        List<Child> childList = List.of(child);
        ChildView childView = new ChildView();
        List<ChildView> childViewList = List.of(childView);

        when(childRepository.findByParentId(parentId)).thenReturn(childList);
        when(childMapper.mapChildView(child)).thenReturn(childView);

        List<ChildView> childViewListResult = childService.getChildrenByParentId(parentId);

        assertThat(childViewListResult).isEqualTo(childViewList);
    }

    @Test
    void addChild() {
        long parentId = 123;
        String name = "Natalia";
        LocalDate birthDate = LocalDate.of(2017, 2, 16);
        Gender gender = Gender.FEMALE;

        ChildData childData = new ChildData();
        childData.setName(name);
        childData.setBirthDate(birthDate);
        childData.setGender(gender);
        UserData userData = new UserData();
        childData.setUserData(userData);

        Parent parent = new Parent();
        when(parentRepository.findById(parentId)).thenReturn(Optional.of(parent));

        User user = new User();
        when(userService.addUser(childData.getUserData())).thenReturn(user);

        childService.addChild(parentId, childData);

        verify(childRepository).save(childArgumentCaptor.capture());
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(childCaptured.getName()).isEqualTo(name);
        assertThat(childCaptured.getBirthDate()).isEqualTo(birthDate);
        assertThat(childCaptured.getGender()).isEqualTo(gender);
        assertThat(childCaptured.getParent()).isEqualTo(parent);
        assertThat(childCaptured.getPoints()).isZero();
        assertThat(childCaptured.getUser()).isEqualTo(user);
    }

    @Test
    void updateChild() {
        long id = 123;
        Gender gender = Gender.FEMALE;
        LocalDate birthDate = LocalDate.of(2017, 2, 16);
        String name = "Natalia";
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

        when(childRepository.findById(id)).thenReturn(Optional.of(child));

        childService.updateChild(id, childData);

        verify(childRepository).save(childArgumentCaptor.capture());
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(childCaptured.getGender()).isEqualTo(gender);
        assertThat(childCaptured.getBirthDate()).isEqualTo(birthDate);
        assertThat(childCaptured.getName()).isEqualTo(name);
    }

    @Test
    void deleteChild() {
        long id = 123;

        Child child = new Child();
        User user = new User();
        user.setId(1L);
        child.setUser(user);
        when(childRepository.findById(id)).thenReturn(Optional.of(child));

        childService.deleteChild(id);

        verify(userRepository).deleteById(user.getId());
        verify(childRepository).deleteById(id);
    }
}