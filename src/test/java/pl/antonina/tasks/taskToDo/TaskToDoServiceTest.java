package pl.antonina.tasks.taskToDo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.cart.HistoryService;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.task.TaskRepository;

import java.security.Principal;
import java.time.Instant;
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
        taskToDoService = new TaskToDoService(taskToDoRepository, taskRepository, childRepository, taskToDoMapper, historyService, loggedUserService);
    }

    @Test
    void getTasksToDoChildIdExists() {
        long childId = 123;
        TaskToDo taskToDo = new TaskToDo();
        List<TaskToDo> taskToDoList = List.of(taskToDo);
        TaskToDoView taskToDoView = new TaskToDoView();
        List<TaskToDoView> taskToDoViewList = List.of(taskToDoView);

        Principal principal = mock(Principal.class);
        Child child = new Child();
        child.setId(childId);
        when(taskToDoRepository.findByChildIdAndApprovedOrderByExpectedDateDesc(childId, false))
                .thenReturn(taskToDoList);
        when(taskToDoMapper.mapTaskToDoView(taskToDo)).thenReturn(taskToDoView);

        List<TaskToDoView> taskToDoViewListResult = taskToDoService.getTasksToDoByChildAndNotApproved(childId, principal);

        assertThat(taskToDoViewListResult).isEqualTo(taskToDoViewList);
    }

    @Test
    void getTasksToDoChildIdIsNull() {
        TaskToDo taskToDo = new TaskToDo();
        List<TaskToDo> taskToDoList = List.of(taskToDo);
        TaskToDoView taskToDoView = new TaskToDoView();
        List<TaskToDoView> taskToDoViewList = List.of(taskToDoView);

        Principal principal = mock(Principal.class);
        Child child = new Child();
        long childId = 123;
        child.setId(childId);
        when(loggedUserService.getChild(principal)).thenReturn(child);

        when(taskToDoRepository.findByChildIdAndApprovedOrderByExpectedDateDesc(childId, false))
                .thenReturn(taskToDoList);
        when(taskToDoMapper.mapTaskToDoView(taskToDo)).thenReturn(taskToDoView);

        List<TaskToDoView> taskToDoViewListResult = taskToDoService.getTasksToDoByChildAndNotApproved(null, principal);

        assertThat(taskToDoViewListResult).isEqualTo(taskToDoViewList);
    }

    @Test
    void addTaskToDo() {
        long childId = 123;
        long taskId = 987;
        Instant expectedDate = Instant.now();
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(expectedDate);

        Child child = new Child();
        Task task = new Task();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskToDoService.addTaskToDo(childId, taskId, taskToDoData);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.getExpectedDate()).isEqualTo(expectedDate);
        assertThat(taskToDoCaptured.getTask()).isEqualTo(task);
        assertThat(taskToDoCaptured.getChild()).isEqualTo(child);
    }

    @Test
    void updateTaskToDo() {
        long id = 123;
        Instant expectedDate = Instant.now();
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(expectedDate);

        TaskToDo taskToDo = new TaskToDo();
        when(taskToDoRepository.findById(id)).thenReturn(Optional.of(taskToDo));

        taskToDoService.updateTaskToDo(id, taskToDoData);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.getExpectedDate()).isEqualTo(expectedDate);
    }

    @Test
    void deleteTaskToDo() {
        long id = 123;
        taskToDoService.deleteTaskToDo(id);
        verify(taskToDoRepository).deleteById(id);
    }

    @Test
    void setDone() {
        long id = 123;

        TaskToDo taskToDo = new TaskToDo();
        when(taskToDoRepository.findById(id)).thenReturn(Optional.of(taskToDo));

        taskToDoService.setDone(id);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.isDone()).isEqualTo(true);
    }

    @Test
    void setApproved() {
        long id = 123;
        Integer points = 20;
        Integer childPoints = 100;
        long childId = 987;

        Child child = new Child();
        child.setId(childId);
        child.setPoints(childPoints);
        Task task = new Task();
        task.setPoints(points);
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setApproved(true);
        taskToDo.setTask(task);
        taskToDo.setChild(child);

        when(taskToDoRepository.findById(id)).thenReturn(Optional.of(taskToDo));
        when(childRepository.findById(childId)).thenReturn(Optional.of(child));

        taskToDoService.setApproved(id);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        verify(childRepository).save(childArgumentCaptor.capture());
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.isApproved()).isTrue();
        assertThat(childCaptured.getPoints()).isEqualTo(points + childPoints);

    }
}