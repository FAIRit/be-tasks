package pl.antonina.tasks.task;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskView> getByParentIdAndByName(@PathVariable long parentId, @RequestParam(required = false) String name) {
        if (name != null) {
            return taskService.getByParentAndName(parentId, name);
        } else {
            return taskService.getByParent(parentId);
        }
    }

    @GetMapping("/{id}")
    public TaskView getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    @PostMapping
    public void addTask(@RequestParam long parentId, @RequestBody TaskData taskData) {
        taskService.addTask(parentId, taskData);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable long id, @RequestBody TaskData taskData) {
        taskService.updateTask(id, taskData);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
