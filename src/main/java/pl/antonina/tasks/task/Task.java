package pl.antonina.tasks.task;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Integer points;
}
