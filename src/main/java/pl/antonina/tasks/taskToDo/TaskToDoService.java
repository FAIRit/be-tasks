package pl.antonina.tasks.taskToDo;

import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

public interface TaskToDoService {

    TaskToDoView getTaskToDoById(long taskToDoId);

    List<TaskToDoView> getTasksToDoByChildAndNotApproved(Principal childPrincipal);

    List<TaskToDoView> getTasksToDoByChildAndNotApproved(long childId);

    void addTaskToDo(long childId, long taskId, TaskToDoData taskToDoData);

    void updateTaskToDo(long taskToDoId, TaskToDoData taskToDoData);

    void deleteTaskToDo(long taskToDoId);

    void setDone(long taskToDoId);

    @Transactional
    void setApproved(long taskToDoId);
}