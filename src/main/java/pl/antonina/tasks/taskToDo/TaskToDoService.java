package pl.antonina.tasks.taskToDo;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildService;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentService;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.task.TaskService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TaskToDoService {

    private TaskToDoRepository taskToDoRepository;
    private TaskService taskService;
    private ChildService childService;

    public TaskToDoService(TaskToDoRepository taskToDoRepository, TaskService taskService, ChildService childService) {
        this.taskToDoRepository = taskToDoRepository;
        this.taskService = taskService;
        this.childService = childService;
    }

    public TaskToDo getTaskToDo(Long id) {
        return taskToDoRepository.findById(id).orElseThrow();
    }

    public List<TaskToDo> getTasksToDoByChildByDoneByApproved(Long childId, boolean done, boolean approved) {
        return taskToDoRepository.findByChildIdAndDoneAndApprovedOrderByExpectedDateDesc(childId, done, approved);
    }

    public void addTaskToDo(Long childId, Long taskId, TaskToDoData taskToDoData) {
        Child child = childService.getChild(childId);
        Task task = taskService.getTask(taskId);
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setTask(task);
        taskToDo.setChild(child);
        //taskToDo.setStartDate(TODAY!!!);
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        taskToDo.setFinishDate(null);
        taskToDo.setDone(false);
        taskToDo.setApproved(false);
        taskToDoRepository.save(taskToDo);
    }

    public void updateTaskToDo(Long id, TaskToDoData taskToDoData) {
        TaskToDo taskToDo = getTaskToDo(id);
        taskToDo.setFinishDate(taskToDoData.getFinishDate());
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        //taskToDo.setDone(taskToDoData.get); ????
        //taskToDo.setApproved();
        taskToDoRepository.save(taskToDo);
    }

    public void deleteTaskToDo(Long id) {
        taskToDoRepository.deleteById(id);
    }
}
