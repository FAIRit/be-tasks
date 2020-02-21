package pl.antonina.tasks.taskToDo;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.task.TaskRepository;

import java.util.List;

@Service
public class TaskToDoService {

    private TaskToDoRepository taskToDoRepository;
    private TaskRepository taskRepository;
    private ChildRepository childRepository;

    public TaskToDoService(TaskToDoRepository taskToDoRepository, TaskRepository taskRepository, ChildRepository childRepository) {
        this.taskToDoRepository = taskToDoRepository;
        this.taskRepository = taskRepository;
        this.childRepository = childRepository;
    }

    public TaskToDo getTaskToDo(Long id) {
        return taskToDoRepository.findById(id).orElseThrow();
    }

    public List<TaskToDo> getTasksToDoByChildByDoneByApproved(Long childId, boolean done, boolean approved) {
        return taskToDoRepository.findByChildIdAndDoneAndApprovedOrderByExpectedDateDesc(childId, done, approved);
    }

    public void addTaskToDo(Long childId, Long taskId, TaskToDoData taskToDoData) {
        Child child = childRepository.findById(childId).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();
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
