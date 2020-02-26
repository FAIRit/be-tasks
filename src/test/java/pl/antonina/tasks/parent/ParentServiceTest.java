package pl.antonina.tasks.parent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.user.Gender;

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
    void addParent() {
        ParentData parentData = new ParentData();
        Gender gender = Gender.FEMALE;
        String name = "Antonina";
        parentData.setGender(gender);
        parentData.setName(name);

        parentService.addParent(parentData);

        verify(parentRepository).save(parentArgumentCaptor.capture());
        Parent parentCaptured = parentArgumentCaptor.getValue();

        assertThat(parentCaptured.getName()).isEqualTo(name);
        assertThat(parentCaptured.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    void updateParent() {
        long id = 123;
        ParentData parentData = new ParentData();
        String name = "Michał";
        Gender gender = Gender.MALE;
        parentData.setName(name);
        parentData.setGender(gender);

        when(parentRepository.findById(id)).thenReturn(Optional.of(new Parent()));

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

    @Test
    void getParent() {
        long id = 123;

        Parent parent = mock(Parent.class);
        ParentView parentView = mock(ParentView.class);

        when(parentRepository.findById(id)).thenReturn(Optional.of(parent));
        when(parentMapper.mapParentView(parent)).thenReturn(parentView);

        ParentView parentViewResult = parentService.getParent(id);
        assertThat(parentViewResult).isEqualTo(parentView);
    }
}