package pl.antonina.tasks.task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskView mapTaskView(Task task) {
        TaskView taskView = new TaskView();
        taskView.setId(task.getId());
        taskView.setName(task.getName());
        taskView.setDescription(task.getDescription());
        taskView.setPoints(task.getPoints());
        return taskView;
    }
}