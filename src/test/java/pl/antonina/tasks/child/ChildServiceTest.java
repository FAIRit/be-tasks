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
import pl.antonina.tasks.user.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(childCaptured.getName()).isEqualTo(name);
        assertThat(childCaptured.getBirthDate()).isEqualTo(birthDate);
        assertThat(childCaptured.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(childCaptured.getParent()).isEqualTo(parent);
        assertThat(childCaptured.getPoints()).isZero();
        //assertThat(child.getUser()).
    }

    @Test
    void addChildNoParent() {
        assertThatThrownBy(() -> childService.addChild(123, new ChildData()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void updateChild() {
        long id = 123;
        ChildData childData = new ChildData();
        Gender gender = Gender.FEMALE;
        LocalDate birthDate = LocalDate.of(2017, 2, 16);
        String name = "Natalia";
        childData.setGender(gender);
        childData.setBirthDate(birthDate);
        childData.setName(name);

        when(childRepository.findById(id)).thenReturn(Optional.of(new Child()));

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
        childService.deleteChild(id);
        verify(childRepository).deleteById(id);
    }

    @Test
    void getChild() {
        long id = 123;

        Child child = mock(Child.class);
        ChildView childView = mock(ChildView.class);

        when(childRepository.findById(id)).thenReturn(Optional.of(child));
        when(childMapper.mapChildView(child)).thenReturn(childView);

        ChildView childViewResult = childService.getChild(id);
        assertThat(childViewResult).isEqualTo(childView);
    }

    @Test
    void getChildrenByParentId() {
        long parentId = 123;
        Child child = mock(Child.class);
        List<Child> childList = List.of(child);
        ChildView childView = mock(ChildView.class);
        List<ChildView> childViewList = List.of(childView);

        when(childRepository.findByParentId(parentId)).thenReturn(childList);
        when(childMapper.mapChildView(child)).thenReturn(childView);

        List<ChildView> childViewListResult = childService.getChildrenByParentId(parentId);

        assertThat(childViewListResult).isEqualTo(childViewList);
    }
}