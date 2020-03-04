package pl.antonina.tasks.task;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskView> getTasksByParent(@ApiIgnore Principal principal) {
        return taskService.getTasksByParent(principal);
    }

    @PostMapping
    public void addTask(@ApiIgnore Principal principal, @RequestBody TaskData taskData) {
        taskService.addTask(principal, taskData);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable long taskId, @RequestBody TaskData taskData) {
        taskService.updateTask(taskId, taskData);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long taskId) {
        taskService.deleteTask(taskId);
    }
}