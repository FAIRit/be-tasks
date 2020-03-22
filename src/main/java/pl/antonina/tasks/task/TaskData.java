package pl.antonina.tasks.task;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class TaskData {
    @NotEmpty
    @Length(min = 3)
    private String name;
    private String description;
    @Min(2)
    private Integer points;
}