package pl.antonina.tasks.taskToDo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.history.HistoryService;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.task.TaskRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskToDoServiceTest {

    @Mock
    private TaskToDoRepository taskToDoRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ChildRepository childRepository;
    @Mock
    private TaskToDoMapper taskToDoMapper;
    @Mock
    private HistoryService historyService;
    @Mock
    private LoggedUserService loggedUserService;

    private TaskToDoService taskToDoService;

    @Captor
    ArgumentCaptor<TaskToDo> taskToDoArgumentCaptor;

    @Captor
    ArgumentCaptor<Child> childArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        taskToDoService = new TaskToDoServiceImpl(taskToDoRepository, taskRepository, childRepository, taskToDoMapper, historyService, loggedUserService);
    }

    @Test
    void getTaskToDoById() {
        long taskToDoId = 1;
        TaskToDo taskToDo = new TaskToDo();
        TaskToDoView taskToDoView = new TaskToDoView();

        Parent parent = new Parent();
        long parentId = 346;
        parent.setId(parentId);

        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(taskToDoRepository.findByIdAndTaskParentId(taskToDoId, parentId)).thenReturn(Optional.of(taskToDo));
        when(taskToDoMapper.mapTaskToDoView(taskToDo)).thenReturn(taskToDoView);

        TaskToDoView taskToDoViewResult = taskToDoService.getTaskToDoById(parentPrincipal, taskToDoId);

        assertThat(taskToDoViewResult).isEqualTo(taskToDoView);
    }

    @Test
    void getTasksToDoByChildId() {
        long childId = 123;
        TaskToDo taskToDo1 = new TaskToDo();
        TaskToDo taskToDo2 = new TaskToDo();
        List<TaskToDo> taskToDoList = List.of(taskToDo1, taskToDo2);
        TaskToDoView taskToDoView1 = new TaskToDoView();
        TaskToDoView taskToDoView2 = new TaskToDoView();
        final List<TaskToDoView> taskToDoViewList = List.of(taskToDoView1, taskToDoView2);

        Child child = new Child();
        child.setId(childId);

        Parent parent = new Parent();
        long parentId = 345;
        parent.setId(parentId);
        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(taskToDoRepository.findByChildIdAndTaskParentIdAndApprovedOrderByExpectedDateDesc(childId, parentId, false))
                .thenReturn(taskToDoList);
        when(taskToDoMapper.mapTaskToDoView(taskToDo1)).thenReturn(taskToDoView1);
        when(taskToDoMapper.mapTaskToDoView(taskToDo2)).thenReturn(taskToDoView2);

        List<TaskToDoView> taskToDoViewListResult = taskToDoService.getTasksToDoByChildAndNotApproved(parentPrincipal, childId);

        assertThat(taskToDoViewListResult).isEqualTo(taskToDoViewList);
    }

    @Test
    void getTasksToDoByChildPrincipal() {
        TaskToDo taskToDo1 = new TaskToDo();
        TaskToDo taskToDo2 = new TaskToDo();
        List<TaskToDo> taskToDoList = List.of(taskToDo1, taskToDo2);
        TaskToDoView taskToDoView1 = new TaskToDoView();
        TaskToDoView taskToDoView2 = new TaskToDoView();
        final List<TaskToDoView> taskToDoViewList = List.of(taskToDoView1, taskToDoView2);

        Principal childPrincipal = mock(Principal.class);
        Child child = new Child();
        long childId = 123;
        child.setId(childId);
        when(loggedUserService.getChild(childPrincipal)).thenReturn(child);

        when(taskToDoRepository.findByChildIdAndApprovedOrderByExpectedDateDesc(childId, false))
                .thenReturn(taskToDoList);
        when(taskToDoMapper.mapTaskToDoView(taskToDo1)).thenReturn(taskToDoView1);
        when(taskToDoMapper.mapTaskToDoView(taskToDo2)).thenReturn(taskToDoView2);

        List<TaskToDoView> taskToDoViewListResult = taskToDoService.getTasksToDoByChildAndNotApproved(childPrincipal);

        assertThat(taskToDoViewListResult).isEqualTo(taskToDoViewList);
    }

    @Test
    void addTaskToDo() {
        long childId = 123;
        long taskId = 987;
        final LocalDate expectedDate = LocalDate.of(2019, 2, 16);
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(expectedDate);

        final Child child = new Child();
        final Task task = new Task();

        Parent parent = new Parent();
        long parentId = 345;
        parent.setId(parentId);

        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(childRepository.findByIdAndParentId(childId, parentId)).thenReturn(Optional.of(child));
        when(taskRepository.findByIdAndParentId(taskId, parentId)).thenReturn(Optional.of(task));
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setId(456L);
        when(taskToDoRepository.save(any())).thenReturn(taskToDo);

        taskToDoService.addTaskToDo(parentPrincipal, childId, taskId, taskToDoData);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.getExpectedDate()).isEqualTo(expectedDate);
        assertThat(taskToDoCaptured.getTask()).isEqualTo(task);
        assertThat(taskToDoCaptured.getChild()).isEqualTo(child);
    }

    @Test
    void updateTaskToDo() {
        long taskToDoId = 123;
        final LocalDate expectedDate = LocalDate.of(2019, 2, 16);
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(expectedDate);

        TaskToDo taskToDo = new TaskToDo();

        Parent parent = new Parent();
        long parentId = 345;
        parent.setId(parentId);

        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(taskToDoRepository.findByIdAndTaskParentId(taskToDoId, parentId)).thenReturn(Optional.of(taskToDo));

        taskToDoService.updateTaskToDo(parentPrincipal, taskToDoId, taskToDoData);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.getExpectedDate()).isEqualTo(expectedDate);
    }

    @Test
    void deleteTaskToDo() {
        final TaskToDo taskToDo = new TaskToDo();
        final long taskToDoId = 123;
        taskToDo.setId(taskToDoId);

        Parent parent = new Parent();
        long parentId = 345;
        parent.setId(parentId);

        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(taskToDoRepository.findByIdAndTaskParentId(taskToDoId, parentId)).thenReturn(Optional.of(taskToDo));

        taskToDoService.deleteTaskToDo(parentPrincipal, taskToDoId);

        verify(taskToDoRepository).delete(taskToDo);
    }

    @Test
    void setDoneFirstTime() {
        long taskToDoId = 123;
        TaskToDo taskToDo = new TaskToDo();

        Child child = new Child();
        long childId = 345;
        child.setId(childId);

        Principal childPrincipal = mock(Principal.class);
        when(loggedUserService.getChild(childPrincipal)).thenReturn(child);
        when(taskToDoRepository.findByIdAndChildId(taskToDoId, childId)).thenReturn(Optional.of(taskToDo));

        taskToDoService.setDone(childPrincipal, taskToDoId);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.isDone()).isEqualTo(true);
    }

    @Test
    void setDoneExists() {
        long taskToDoId = 123;
        boolean done = true;
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setDone(done);
        Child child = new Child();
        long childId = 345;
        child.setId(childId);

        Principal childPrincipal = mock(Principal.class);
        when(loggedUserService.getChild(childPrincipal)).thenReturn(child);
        when(taskToDoRepository.findByIdAndChildId(taskToDoId, childId)).thenReturn(Optional.of(taskToDo));

        taskToDoService.setDone(childPrincipal, taskToDoId);

        verify(taskToDoRepository, never()).save(any());
    }

    @Test
    void setApproved() {
        long taskToDoId = 123;
        final Integer taskPoints = 20;
        final Integer childPoints = 100;
        long childId = 987;

        Child child = new Child();
        child.setId(childId);
        child.setPoints(childPoints);
        Task task = new Task();
        task.setPoints(taskPoints);
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setApproved(true);
        taskToDo.setTask(task);
        taskToDo.setChild(child);

        Parent parent = new Parent();
        long parentId = 345;
        parent.setId(parentId);

        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(taskToDoRepository.findByIdAndTaskParentId(taskToDoId, parentId)).thenReturn(Optional.of(taskToDo));
        when(childRepository.findByIdAndParentId(childId, parentId)).thenReturn(Optional.of(child));

        taskToDoService.setApproved(parentPrincipal, taskToDoId);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        verify(childRepository).save(childArgumentCaptor.capture());
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.isApproved()).isTrue();
        assertThat(childCaptured.getPoints()).isEqualTo(taskPoints + childPoints);
    }
}