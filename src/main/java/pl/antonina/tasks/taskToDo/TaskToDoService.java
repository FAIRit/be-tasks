package pl.antonina.tasks.taskToDo;

import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

public interface TaskToDoService {

    TaskToDoView getTaskToDoById(Principal parentPrincipal, long taskToDoId);

    List<TaskToDoView> getTasksToDoByChildAndNotApproved(Principal childPrincipal);

    List<TaskToDoView> getTasksToDoByChildAndNotApproved(Principal parentPrincipal, long childId);

    void addTaskToDo(Principal parentPrincipal, long childId, long taskId, TaskToDoData taskToDoData);

    void updateTaskToDo(Principal parentPrincipal, long taskToDoId, TaskToDoData taskToDoData);

    void deleteTaskToDo(Principal parentPrincipal, long taskToDoId);

    void setDone(Principal childPrincipal, long taskToDoId);

    @Transactional
    void setApproved(Principal parentPrincipal, long taskToDoId);
}