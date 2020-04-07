package pl.antonina.tasks.task;

import java.security.Principal;
import java.util.List;

public interface TaskService {

    TaskView getTask(Principal parentPrincipal, long taskId);

    List<TaskView> getTasksByParent(Principal parentPrincipal);

    long addTask(Principal parentPrincipal, TaskData taskData);

    void updateTask(Principal parentPrincipal, long taskId, TaskData taskData);

    void deleteTask(Principal parentPrincipal, long taskId);
}