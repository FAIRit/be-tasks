package pl.antonina.tasks.taskToDo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.task.TaskMapper;
import pl.antonina.tasks.task.TaskView;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskToDoMapperTest {

    @Mock
    private TaskMapper taskMapper;

    private TaskToDoMapper taskToDoMapper;

    @BeforeEach
    void beforeEach() {
        taskToDoMapper = new TaskToDoMapper(taskMapper);
    }

    @Test
    void mapTaskToDoView() {
        Instant expectedDate = Instant.now().minusSeconds(30);
        Instant finishDate = Instant.now().minusSeconds(20);
        Instant startDate = Instant.now().minusSeconds(10);
        boolean done = false;
        boolean approved = true;
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setExpectedDate(expectedDate);
        taskToDo.setStartDate(startDate);
        taskToDo.setFinishDate(finishDate);
        taskToDo.setDone(done);
        taskToDo.setApproved(approved);

        Task task = new Task();
        taskToDo.setTask(task);

        TaskView taskView = mock(TaskView.class);
        when(taskMapper.mapTaskView(task)).thenReturn(taskView);

        TaskToDoView taskToDoView = taskToDoMapper.mapTaskToDoView(taskToDo);

        assertThat(taskToDoView.getExpectedDate()).isEqualTo(expectedDate);
        assertThat(taskToDoView.getFinishDate()).isEqualTo(finishDate);
        assertThat(taskToDoView.getStartDate()).isEqualTo(startDate);
        assertThat(taskToDoView.getTaskView()).isEqualTo(taskView);
        assertThat(taskToDoView.isDone()).isEqualTo(done);
        assertThat(taskToDoView.isApproved()).isEqualTo(approved);
    }
}