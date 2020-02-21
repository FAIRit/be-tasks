package pl.antonina.tasks.task;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents/{parentId}/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getByParentId(@PathVariable Long parentId){
        return taskService.getByParent(parentId);
    }

    /*
    @GetMapping
    public List<Task> getByParentIdByName(@PathVariable Long parentId, @RequestParam String name){
        return taskService.getByParentByName(parentId, name);
    }
     */

    @GetMapping("/{id}")
    public Task getTask(@PathVariable Long parentId, @PathVariable Long id){
        return taskService.getTask(id);
    }

    @PostMapping
    public void addTask(@PathVariable Long parentId, @RequestBody TaskData taskData){
        taskService.addTask(parentId, taskData);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long parentId,@PathVariable Long id, @RequestBody TaskData taskData){
        taskService.updateTask(parentId, id, taskData);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long parentId, @PathVariable Long id){
        taskService.deleteTask(id);
    }
}
