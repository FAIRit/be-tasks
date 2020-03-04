package pl.antonina.tasks.taskToDo;

import lombok.Data;
import pl.antonina.tasks.task.TaskView;

import java.time.Instant;

@Data
public class TaskToDoView {
    private TaskView taskView;
    private Instant startDate;
    private Instant expectedDate;
    private Instant finishDate;
    private boolean done;
}
