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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParentServiceTest {

    @Mock
    private ParentMapper parentMapper;
    @Mock
    private ParentRepository parentRepository;

    private ParentService parentService;

    @Captor
    private ArgumentCaptor<Parent> parentArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        parentService = new ParentService(parentRepository, parentMapper);
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
        assertThat(parentCaptured.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(parentCaptured.getUser().getPassword()).isEqualTo(password);
        assertThat(parentCaptured.getUser().getEmail()).isEqualTo(email);
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
    void deleteById() {
        long id = 123;
        parentService.deleteParent(id);
        verify(parentRepository).deleteById(id);
    }
}