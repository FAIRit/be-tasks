package pl.antonina.tasks.task;

import lombok.Data;
import pl.antonina.tasks.parent.Parent;

@Data
public class TaskToGet {
    private String name;
    private String description;
    private Integer points;
}
