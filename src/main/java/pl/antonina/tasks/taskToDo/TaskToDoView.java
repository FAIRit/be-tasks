package pl.antonina.tasks.taskToDo;

import lombok.Data;
import pl.antonina.tasks.task.TaskView;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class TaskToDoView {
    private long id;
    private TaskView taskView;
    private Instant startDate;
    private LocalDate expectedDate;
    private Instant finishDate;
    private boolean done;
}
