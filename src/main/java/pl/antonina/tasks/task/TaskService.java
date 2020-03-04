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

    List<TaskView> getTasksByParent(Principal principal) {
        Parent parent = loggedUserService.getParent(principal);
        return taskRepository.findByParentIdOrderByNameAsc(parent.getId()).stream()
                .map(taskMapper::mapTaskView)
                .collect(Collectors.toList());
    }

    void addTask(Principal principal, TaskData taskData) {
        Parent parent = loggedUserService.getParent(principal);
        Task task = new Task();
        mapTask(taskData, task);
        task.setParent(parent);
        taskRepository.save(task);
    }

    void updateTask(long taskId, TaskData taskData) {
        Task task = taskRepository.findById(taskId).orElseThrow();
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