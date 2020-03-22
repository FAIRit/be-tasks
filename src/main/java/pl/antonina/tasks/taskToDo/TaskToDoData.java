package pl.antonina.tasks.taskToDo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class TaskToDoData {

    @NotNull
    @DateTimeFormat
    private LocalDate expectedDate;
}