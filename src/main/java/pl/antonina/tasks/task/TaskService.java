package pl.antonina.tasks.task;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.parent.ParentRepository;
import pl.antonina.tasks.parent.ParentService;

import java.util.List;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private ParentService parentService;

    public TaskService(TaskRepository taskRepository, ParentRepository parentRepository, ParentService parentService) {
        this.taskRepository = taskRepository;
        this.parentService = parentService;
    }

    public List<Task> getByParent(Long parentId) {
        return taskRepository.findByParentIdOrderByNameAsc(parentId);
    }

    public List<Task> getByParentByName(Long parentId, String name) {
        return taskRepository.findByParentIdAndNameContains(parentId, name);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    public void addTask(Long parentId, TaskData taskData) {
        Parent parent = parentService.getParent(parentId);
        Task task = new Task();
        task.setName(taskData.getName());
        task.setDescription(taskData.getDescription());
        task.setPoints(taskData.getPoints());
        task.setParent(parent);
        taskRepository.save(task);
    }

    public void updateTask(Long parentId, Long id, TaskData taskData) {
        Parent parent = parentService.getParent(parentId);
        Task task = taskRepository.findById(id).orElseThrow();
        task.setName(taskData.getName());
        task.setDescription(taskData.getDescription());
        task.setPoints(taskData.getPoints());
        taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
