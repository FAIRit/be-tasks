package pl.antonina.tasks.taskToDo;

import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.task.Task;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tasksToDo")
public class TaskToDo {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Child child;
    @ManyToOne
    private Task task;
    private Instant startDate;
    private Instant expectedDate;
    private Instant finishDate;
    private boolean done;
    private boolean approved;
}