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
        Task task = new Task();
        List<Task> taskList = List.of(task);
        TaskView taskView = new TaskView();
        List<TaskView> taskViewList = List.of(taskView);

        Principal principal = mock(Principal.class);
        when(loggedUserService.getParent(principal)).thenReturn(parent);
        when(taskRepository.findByParentIdOrderByNameAsc(parentId)).thenReturn(taskList);
        when(taskMapper.mapTaskView(task)).thenReturn(taskView);

        List<TaskView> taskViewListResult = taskService.getTasksByParent(principal);

        assertThat(taskViewListResult).isEqualTo(taskViewList);
    }

    @Test
    void addTask() {
        String name = "Sprzątanie zabawek";
        String description = "Cały pokój";
        Integer points = 10;
        TaskData taskData = new TaskData();
        taskData.setName(name);
        taskData.setDescription(description);
        taskData.setPoints(points);

        Principal principal = mock(Principal.class);
        Parent parent = new Parent();
        when(loggedUserService.getParent(principal)).thenReturn(parent);

        taskService.addTask(principal, taskData);

        verify(taskRepository).save(taskArgumentCaptor.capture());
        Task taskCaptured = taskArgumentCaptor.getValue();

        assertThat(taskCaptured.getName()).isEqualTo(name);
        assertThat(taskCaptured.getDescription()).isEqualTo(description);
        assertThat(taskCaptured.getPoints()).isEqualTo(points);
        assertThat(taskCaptured.getParent()).isEqualTo(parent);
    }

    @Test
    void updateTask() {
        long id = 123;
        Integer points = 10;
        String name = "Sprzątanie zabawek";
        String description = "Cały pokój";
        TaskData taskData = new TaskData();
        taskData.setPoints(points);
        taskData.setName(name);
        taskData.setDescription(description);

        Task task = new Task();
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        taskService.updateTask(id, taskData);

        verify(taskRepository).save(taskArgumentCaptor.capture());
        Task taskCaptured = taskArgumentCaptor.getValue();

        assertThat(taskCaptured.getPoints()).isEqualTo(points);
        assertThat(taskCaptured.getDescription()).isEqualTo(description);
        assertThat(taskCaptured.getName()).isEqualTo(name);
    }

    @Test
    void deleteTask() {
        long id = 987;

        taskService.deleteTask(id);
        verify(taskRepository).deleteById(id);
    }
}