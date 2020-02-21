package pl.antonina.tasks.task;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.parent.ParentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ParentRepository parentRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository,
                       ParentRepository parentRepository,
                       TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.parentRepository = parentRepository;
        this.taskMapper = taskMapper;
    }

    List<TaskView> getByParent(Long parentId) {
        return taskRepository.findByParentIdOrderByNameAsc(parentId).stream()
                .map(taskMapper::mapTaskView)
                .collect(Collectors.toList());
    }

    List<TaskView> getByParentAndName(long parentId, String name) {
        return taskRepository.findByParentIdAndNameContains(parentId, name).stream()
                .map(taskMapper::mapTaskView)
                .collect(Collectors.toList());
    }

    TaskView getTask(long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        return taskMapper.mapTaskView(task);
    }

    void addTask(long parentId, TaskData taskData) {
        Parent parent = parentRepository.findById(parentId).orElseThrow();
        Task task = mapTask(taskData, new Task());
        task.setParent(parent);
        taskRepository.save(task);
    }

    void updateTask(long parentId, long id, TaskData taskData) {
        Task task = taskRepository.findById(id).orElseThrow();
        checkParentId(parentId, task);
        mapTask(taskData, task);
        taskRepository.save(task);
    }

    private void checkParentId(long parentId, Task task) {
        if (!task.getParent().getId().equals(parentId)) {
            throw new IllegalArgumentException("Wrong parentId");
        }
    }

    void deleteTask(long parentId, long id) {
        taskRepository.findById(id).ifPresent(task -> {
            checkParentId(parentId, task);
            taskRepository.delete(task);
        });
    }

    private Task mapTask(TaskData taskData, Task task) {
        task.setName(taskData.getName());
        task.setDescription(taskData.getDescription());
        task.setPoints(taskData.getPoints());
        return task;
    }
}