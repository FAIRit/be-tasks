package pl.antonina.tasks.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    ParentRepository parentRepository;
    @Mock
    TaskMapper taskMapper;

    private TaskService taskService;

    @Captor
    ArgumentCaptor<Task> taskArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        taskService = new TaskService(taskRepository, parentRepository, taskMapper);
    }

    @Test
    void getByParent() {
        long parentId = 123;
        Task task = new Task();
        List<Task> taskList = List.of(task);
        TaskView taskView = new TaskView();
        List<TaskView> taskViewList = List.of(taskView);

        when(taskRepository.findByParentIdOrderByNameAsc(parentId)).thenReturn(taskList);
        when(taskMapper.mapTaskView(task)).thenReturn(taskView);

        List<TaskView> taskViewListResult = taskService.getByParent(parentId);

        assertThat(taskViewListResult).isEqualTo(taskViewList);
    }

    @Test
    void getByParentAndName() {
        long parentId = 123;
        String name = "Antonina";
        Task task = new Task();
        List<Task> taskList = List.of(task);
        TaskView taskView = new TaskView();
        List<TaskView> taskViewList = List.of(taskView);

        when(taskRepository.findByParentIdAndNameContains(parentId, name)).thenReturn(taskList);
        when(taskMapper.mapTaskView(task)).thenReturn(taskView);

        List<TaskView> taskViewListResult = taskService.getByParentAndName(parentId, name);

        assertThat(taskViewListResult).isEqualTo(taskViewList);
    }

    @Test
    void getTask() {
        long id = 123;
        Task task = mock(Task.class);
        TaskView taskView = mock(TaskView.class);

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskMapper.mapTaskView(task)).thenReturn(taskView);

        TaskView taskViewResult = taskService.getTask(id);

        assertThat(taskViewResult).isEqualTo(taskView);
    }

    @Test
    void addTask() {
        long parentId = 123;
        String name = "Sprzątanie zabawek";
        String description = "Cały pokój";
        Integer points = 10;
        TaskData taskData = new TaskData();
        taskData.setName(name);
        taskData.setDescription(description);
        taskData.setPoints(points);

        Parent parent = new Parent();
        when(parentRepository.findById(parentId)).thenReturn(Optional.of(parent));
        taskService.addTask(parentId, taskData);

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