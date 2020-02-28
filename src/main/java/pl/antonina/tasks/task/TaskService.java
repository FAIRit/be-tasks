package pl.antonina.tasks.task;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
class TaskService {

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

    List<TaskView> getByParent(long parentId) {
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
        Task task = new Task();
        mapTask(taskData, task);
        task.setParent(parent);
        taskRepository.save(task);
    }

    void updateTask(long id, TaskData taskData) {
        Task task = taskRepository.findById(id).orElseThrow();
        mapTask(taskData, task);
        taskRepository.save(task);
    }

    void deleteTask(long id) {
        taskRepository.deleteById(id);
    }

    private void mapTask(TaskData taskData, Task task) {
        task.setName(taskData.getName());
        task.setDescription(taskData.getDescription());
        task.setPoints(taskData.getPoints());
    }
}