package pl.antonina.tasks.taskToDo;

import lombok.Data;

import java.time.Instant;

@Data
public class TaskToDoData {

    private Instant startDate;
    private Instant expectedDate;
    private Instant finishDate;
    private boolean done;
    private boolean approved;
}
