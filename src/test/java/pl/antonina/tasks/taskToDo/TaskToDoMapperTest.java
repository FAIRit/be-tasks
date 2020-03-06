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
        final Instant expectedDate = Instant.now().minusSeconds(30);
        final Instant finishDate = Instant.now().minusSeconds(20);
        final Instant startDate = Instant.now().minusSeconds(10);
        final boolean done = false;
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setExpectedDate(expectedDate);
        taskToDo.setStartDate(startDate);
        taskToDo.setFinishDate(finishDate);
        taskToDo.setDone(done);

        final TaskView taskView = new TaskView();
        when(taskMapper.mapTaskView(taskToDo.getTask())).thenReturn(taskView);

        TaskToDoView taskToDoView = taskToDoMapper.mapTaskToDoView(taskToDo);

        assertThat(taskToDoView.getExpectedDate()).isEqualTo(expectedDate);
        assertThat(taskToDoView.getFinishDate()).isEqualTo(finishDate);
        assertThat(taskToDoView.getStartDate()).isEqualTo(startDate);
        assertThat(taskToDoView.isDone()).isEqualTo(done);
        assertThat(taskToDoView.getTaskView()).isEqualTo(taskView);
    }
}