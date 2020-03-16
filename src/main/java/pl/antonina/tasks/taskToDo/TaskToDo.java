package pl.antonina.tasks.taskToDo;

import lombok.Data;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.task.Task;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "tasksToDo")
@Data
public class TaskToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasksToDo_gen")
    @SequenceGenerator(name = "tasksToDo_gen", sequenceName = "tasksToDo_sequence", allocationSize = 1)
    private Long id;
    @ManyToOne
    private Child child;
    @ManyToOne
    private Task task;
    private Instant startDate;
    private LocalDate expectedDate;
    private Instant finishDate;
    private boolean done;
    private boolean approved;
}