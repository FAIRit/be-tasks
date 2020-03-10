package pl.antonina.tasks.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.security.LoggedUserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    TaskMapper taskMapper;
    @Mock
    LoggedUserService loggedUserService;


    private TaskService taskService;

    @Captor
    ArgumentCaptor<Task> taskArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        taskService = new TaskService(taskRepository, taskMapper, loggedUserService);
    }

    @Test
    void getTasksByParent() {
        long parentId = 123;
        Parent parent = new Parent();
        parent.setId(parentId);
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> taskList = List.of(task1, task2);
        TaskView taskView1 = new TaskView();
        TaskView taskView2 = new TaskView();
        final List<TaskView> taskViewList = List.of(taskView1, taskView2);

        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(taskRepository.findByParentIdOrderByNameAsc(parentId)).thenReturn(taskList);
        when(taskMapper.mapTaskView(task1)).thenReturn(taskView1);
        when(taskMapper.mapTaskView(task2)).thenReturn(taskView2);

        List<TaskView> taskViewListResult = taskService.getTasksByParent(parentPrincipal);

        assertThat(taskViewListResult).isEqualTo(taskViewList);
    }

    @Test
    void addTask() {
        final String name = "Sprzątanie zabawek";
        final String description = "Cały pokój";
        final Integer points = 10;
        TaskData taskData = new TaskData();
        taskData.setName(name);
        taskData.setDescription(description);
        taskData.setPoints(points);

        Principal parentPrincipal = mock(Principal.class);
        final Parent parent = new Parent();
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);

        taskService.addTask(parentPrincipal, taskData);

        verify(taskRepository).save(taskArgumentCaptor.capture());
        Task taskCaptured = taskArgumentCaptor.getValue();

        assertThat(taskCaptured.getName()).isEqualTo(name);
        assertThat(taskCaptured.getDescription()).isEqualTo(description);
        assertThat(taskCaptured.getPoints()).isEqualTo(points);
        assertThat(taskCaptured.getParent()).isEqualTo(parent);
    }

    @Test
    void updateTask() {
        long taskId = 123;
        final Integer points = 10;
        final String name = "Sprzątanie zabawek";
        final String description = "Cały pokój";
        TaskData taskData = new TaskData();
        taskData.setPoints(points);
        taskData.setName(name);
        taskData.setDescription(description);

        Task task = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.updateTask(taskId, taskData);

        verify(taskRepository).save(taskArgumentCaptor.capture());
        Task taskCaptured = taskArgumentCaptor.getValue();

        assertThat(taskCaptured.getPoints()).isEqualTo(points);
        assertThat(taskCaptured.getDescription()).isEqualTo(description);
        assertThat(taskCaptured.getName()).isEqualTo(name);
    }

    @Test
    void deleteTask() {
        final long taskId = 987;

        taskService.deleteTask(taskId);
        verify(taskRepository).deleteById(taskId);
    }
}