package pl.antonina.tasks.child;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.user.Gender;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {

    @Mock
    private ChildRepository childRepository;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private ChildMapper childMapper;

    private ChildService childService;

    @Captor
    private ArgumentCaptor<Child> childArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        childService = new ChildService(childRepository, parentRepository, childMapper);
    }

    @Test
    void addChild() {
        long parentId = 123;
        LocalDate birthDate = LocalDate.of(2017, 2, 16);
        ChildData childData = new ChildData();
        String name = "Natalia";
        childData.setName(name);
        childData.setBirthDate(birthDate);
        childData.setGender(Gender.FEMALE);

        Parent parent = mock(Parent.class);
        when(parentRepository.findById(parentId)).thenReturn(Optional.of(parent));

        childService.addChild(parentId, childData);

        verify(childRepository).save(childArgumentCaptor.capture());
        Child child = childArgumentCaptor.getValue();

        assertThat(child.getName()).isEqualTo(name);
        assertThat(child.getBirthDate()).isEqualTo(birthDate);
        assertThat(child.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(child.getParent()).isEqualTo(parent);
        assertThat(child.getPoints()).isZero();
    }

    @Test
    void addChildNoParent() {
        assertThatThrownBy(() -> childService.addChild(123, new ChildData()))
                .isInstanceOf(NoSuchElementException.class);
    }
}