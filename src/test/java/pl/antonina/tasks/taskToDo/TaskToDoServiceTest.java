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
import pl.antonina.tasks.task.TaskView;

import java.security.Principal;
import java.time.Instant;
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
        taskToDoService = new TaskToDoService(taskToDoRepository, taskRepository, childRepository, taskToDoMapper, historyService, loggedUserService);
    }

    @Test
    void getTaskToDoById(){
        long taskToDoId = 1;
        TaskToDo taskToDo = new TaskToDo();
        TaskToDoView taskToDoView = new TaskToDoView();

        when(taskToDoRepository.findById(taskToDoId)).thenReturn(Optional.of(taskToDo));
        when(taskToDoMapper.mapTaskToDoView(taskToDo)).thenReturn(taskToDoView);

        TaskToDoView taskToDoViewResult = taskToDoService.getTaskToDoById(taskToDoId);

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
        when(taskToDoRepository.findByChildIdAndApprovedOrderByExpectedDateDesc(childId, false))
                .thenReturn(taskToDoList);
        when(taskToDoMapper.mapTaskToDoView(taskToDo1)).thenReturn(taskToDoView1);
        when(taskToDoMapper.mapTaskToDoView(taskToDo2)).thenReturn(taskToDoView2);

        List<TaskToDoView> taskToDoViewListResult = taskToDoService.getTasksToDoByChildAndNotApproved(childId);

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
        long taskToDoId = 123;
        final LocalDate expectedDate = LocalDate.of(2019, 2, 16);
        TaskToDoData taskToDoData = new TaskToDoData();
        taskToDoData.setExpectedDate(expectedDate);

        TaskToDo taskToDo = new TaskToDo();
        when(taskToDoRepository.findById(taskToDoId)).thenReturn(Optional.of(taskToDo));

        taskToDoService.updateTaskToDo(taskToDoId, taskToDoData);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.getExpectedDate()).isEqualTo(expectedDate);
    }

    @Test
    void deleteTaskToDo() {
        final long taskToDoId = 123;
        taskToDoService.deleteTaskToDo(taskToDoId);
        verify(taskToDoRepository).deleteById(taskToDoId);
    }

    @Test
    void setDoneFirstTime() {
        long taskToDoId = 123;

        TaskToDo taskToDo = new TaskToDo();
        when(taskToDoRepository.findById(taskToDoId)).thenReturn(Optional.of(taskToDo));

        taskToDoService.setDone(taskToDoId);

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

        when(taskToDoRepository.findById(taskToDoId)).thenReturn(Optional.of(taskToDo));

        taskToDoService.setDone(taskToDoId);

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

        when(taskToDoRepository.findById(taskToDoId)).thenReturn(Optional.of(taskToDo));
        when(childRepository.findById(childId)).thenReturn(Optional.of(child));

        taskToDoService.setApproved(taskToDoId);

        verify(taskToDoRepository).save(taskToDoArgumentCaptor.capture());
        TaskToDo taskToDoCaptured = taskToDoArgumentCaptor.getValue();

        verify(childRepository).save(childArgumentCaptor.capture());
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(taskToDoCaptured.isApproved()).isTrue();
        assertThat(childCaptured.getPoints()).isEqualTo(taskPoints + childPoints);

    }
}