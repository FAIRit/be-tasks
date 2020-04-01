package pl.antonina.tasks.task;

import java.security.Principal;
import java.util.List;

public interface TaskService {

    Task getTask(Principal parentPrincipal, long taskId);

    List<TaskView> getTasksByParent(Principal parentPrincipal);

    void addTask(Principal parentPrincipal, TaskData taskData);

    void updateTask(Principal parentPrincipal, long taskId, TaskData taskData);

    void deleteTask(Principal parentPrincipal, long taskId);
}