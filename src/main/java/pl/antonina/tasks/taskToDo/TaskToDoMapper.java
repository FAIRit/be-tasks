package pl.antonina.tasks.taskToDo;

import org.springframework.stereotype.Component;
import pl.antonina.tasks.task.TaskMapper;

@Component
public class TaskToDoMapper {

    private final TaskMapper taskMapper;

    public TaskToDoMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public TaskToDoView mapTaskToDoView(TaskToDo taskToDo) {
        TaskToDoView taskToDoView = new TaskToDoView();
        taskToDoView.setTaskView(taskMapper.mapTaskView(taskToDo.getTask()));
        taskToDoView.setExpectedDate(taskToDo.getExpectedDate());
        taskToDoView.setStartDate(taskToDo.getStartDate());
        taskToDoView.setFinishDate(taskToDo.getFinishDate());
        taskToDoView.setDone(taskToDo.isDone());
        return taskToDoView;
    }
}
