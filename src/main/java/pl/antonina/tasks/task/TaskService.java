package pl.antonina.tasks.task;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.parent.ParentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private ParentService parentService;

    public TaskService(TaskRepository taskRepository, ParentRepository parentRepository, ParentService parentService) {
        this.taskRepository = taskRepository;
        this.parentService = parentService;
    }

    public List<TaskToGet> getByParent(Long parentId) {
        return taskRepository.findByParentIdOrderByNameAsc(parentId).stream()
                .map(this::mapTaskToGet)
                .collect(Collectors.toList());
    }

    public List<TaskToGet> getByParentAndName(Long parentId, String name) {
        return taskRepository.findByParentIdAndNameContains(parentId, name).stream()
                .map(this::mapTaskToGet)
                .collect(Collectors.toList());
    }

    public TaskToGet getTask(Long id) {
        return mapTaskToGet(taskRepository.findById(id).orElseThrow());
    }

    public void addTask(Long parentId, TaskData taskData) {
        Parent parent = parentService.getParent(parentId);
        Task task = mapTask(taskData, new Task());
        task.setParent(parent);
        taskRepository.save(task);
    }

    public void updateTask(Long parentId, Long id, TaskData taskData) {
        Parent parent = parentService.getParent(parentId);
        Task task = taskRepository.findById(id).orElseThrow();
        taskRepository.save(mapTask(taskData, task));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskToGet mapTaskToGet(Task task) {
        TaskToGet taskToGet = new TaskToGet();
        taskToGet.setName(task.getName());
        taskToGet.setDescription(task.getDescription());
        taskToGet.setPoints(task.getPoints());
        return taskToGet;
    }

    public Task mapTask(TaskData taskData, Task task){
        task.setName(taskData.getName());
        task.setDescription(taskData.getDescription());
        task.setPoints(taskData.getPoints());
        return task;
    }
}
