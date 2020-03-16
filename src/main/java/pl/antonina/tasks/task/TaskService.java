package pl.antonina.tasks.task;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.security.LoggedUserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final LoggedUserService loggedUserService;

    public TaskService(TaskRepository taskRepository,
                       TaskMapper taskMapper,
                       LoggedUserService loggedUserService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.loggedUserService = loggedUserService;
    }

    Task getTask(long taskId){
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotExistsException("Task with given id doesn't exist."));
    }

    List<TaskView> getTasksByParent(Principal parentPrincipal) {
        Parent parent = loggedUserService.getParent(parentPrincipal);
        return taskRepository.findByParentIdOrderByNameAsc(parent.getId()).stream()
                .map(taskMapper::mapTaskView)
                .collect(Collectors.toList());
    }

    void addTask(Principal parentPrincipal, TaskData taskData) {
        Parent parent = loggedUserService.getParent(parentPrincipal);
        Task task = new Task();
        mapTask(taskData, task);
        task.setParent(parent);
        taskRepository.save(task);
    }

    void updateTask(long taskId, TaskData taskData) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotExistsException("Task with given id doesn't exist."));
        mapTask(taskData, task);
        taskRepository.save(task);
    }

    void deleteTask(long taskId) {
        taskRepository.deleteById(taskId);
    }

    private void mapTask(TaskData taskData, Task task) {
        task.setName(taskData.getName());
        task.setDescription(taskData.getDescription());
        task.setPoints(taskData.getPoints());
    }
}