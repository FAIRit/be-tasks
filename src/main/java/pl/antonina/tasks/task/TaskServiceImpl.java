package pl.antonina.tasks.task;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.security.LoggedUserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final LoggedUserService loggedUserService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskMapper taskMapper,
                           LoggedUserService loggedUserService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.loggedUserService = loggedUserService;
    }

    @Override
    public Task getTask(long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotExistsException("Task with id=" + taskId + " doesn't exist."));
    }

    @Override
    public List<TaskView> getTasksByParent(Principal parentPrincipal) {
        Parent parent = loggedUserService.getParent(parentPrincipal);
        return taskRepository.findByParentIdOrderByNameAsc(parent.getId()).stream()
                .map(taskMapper::mapTaskView)
                .collect(Collectors.toList());
    }

    @Override
    public void addTask(Principal parentPrincipal, TaskData taskData) {
        Parent parent = loggedUserService.getParent(parentPrincipal);
        Task task = new Task();
        mapTask(taskData, task);
        task.setParent(parent);
        taskRepository.save(task);
    }

    @Override
    public void updateTask(long taskId, TaskData taskData) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotExistsException("Task with id=" + taskId + " doesn't exist."));
        mapTask(taskData, task);
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(long taskId) {
        taskRepository.deleteById(taskId);
    }

    private void mapTask(TaskData taskData, Task task) {
        task.setName(taskData.getName());
        task.setDescription(taskData.getDescription());
        task.setPoints(taskData.getPoints());
    }
}