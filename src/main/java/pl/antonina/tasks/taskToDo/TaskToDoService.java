package pl.antonina.tasks.taskToDo;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.task.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskToDoService {

    private final TaskToDoRepository taskToDoRepository;
    private final TaskRepository taskRepository;
    private final ChildRepository childRepository;
    private final TaskToDoMapper taskToDoMapper;

    public TaskToDoService(TaskToDoRepository taskToDoRepository,
                           TaskRepository taskRepository,
                           ChildRepository childRepository,
                           TaskToDoMapper taskToDoMapper) {
        this.taskToDoRepository = taskToDoRepository;
        this.taskRepository = taskRepository;
        this.childRepository = childRepository;
        this.taskToDoMapper = taskToDoMapper;
    }

    TaskToDoView getTaskToDo(Long id) {
        TaskToDo taskToDo = taskToDoRepository.findById(id).orElseThrow();
        return taskToDoMapper.mapTaskToDoView(taskToDo);
    }

    List<TaskToDoView> getTasksToDoByChildByDoneByApproved(Long childId, boolean done, boolean approved) {
        List<TaskToDo> taskToDoList = taskToDoRepository.findByChildIdAndDoneAndApprovedOrderByExpectedDateDesc(childId, done, approved);
        return taskToDoList.stream()
                .map(taskToDoMapper::mapTaskToDoView)
                .collect(Collectors.toList());
    }

    void addTaskToDo(Long childId, Long taskId, TaskToDoData taskToDoData) {
        Child child = childRepository.findById(childId).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();
        TaskToDo taskToDo = mapTaskToDo(taskToDoData, new TaskToDo());
        taskToDo.setTask(task);
        taskToDo.setChild(child);
        taskToDoRepository.save(taskToDo);
    }

    void updateTaskToDo(Long id, TaskToDoData taskToDoData) {
        TaskToDo taskToDo = taskToDoRepository.findById(id).orElseThrow();
        mapTaskToDo(taskToDoData, taskToDo);
        taskToDoRepository.save(taskToDo);
    }

    void deleteTaskToDo(Long id) {
        taskToDoRepository.deleteById(id);
    }

    private TaskToDo mapTaskToDo(TaskToDoData taskToDoData, TaskToDo taskToDo) {
        taskToDo.setStartDate(taskToDoData.getStartDate());
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        taskToDo.setFinishDate(taskToDoData.getFinishDate());
        taskToDo.setDone(taskToDoData.isDone());
        taskToDo.setApproved(taskToDoData.isApproved());
        return taskToDo;
    }
}
