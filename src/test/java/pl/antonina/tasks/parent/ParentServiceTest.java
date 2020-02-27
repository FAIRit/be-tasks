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
import pl.antonina.tasks.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        ParentData parentData = new ParentData();
        Gender gender = Gender.FEMALE;
        String name = "Antonina";
        String password = "password";
        String email= "amarikhina@gmail.com";
        parentData.setGender(gender);
        parentData.setName(name);
        parentData.setEmail(email);
        parentData.setPassword(password);

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
        ParentData parentData = new ParentData();
        String name = "Michał";
        Gender gender = Gender.MALE;
        parentData.setName(name);
        parentData.setGender(gender);

        Parent parent = new Parent();
        when(parentRepository.findById(id)).thenReturn(Optional.of(parent));

        parentService.updateParent(id, parentData);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(gender);
    }

    @Test
    void deleteById() {
        long id = 123;
        parentService.deleteParent(id);
        verify(parentRepository).deleteById(id);
    }
}