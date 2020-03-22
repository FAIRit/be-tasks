package pl.antonina.tasks.taskToDo;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasksToDo")
public class TaskToDoController {

    private final TaskToDoService taskToDoService;

    public TaskToDoController(TaskToDoService taskToDoService) {
        this.taskToDoService = taskToDoService;
    }

    @GetMapping("/{taskToDoId}")
    public TaskToDoView getTaskToDoById(@PathVariable long taskToDoId) {
        return taskToDoService.getTaskToDoById(taskToDoId);
    }

    @GetMapping("/byChild")
    public List<TaskToDoView> getTasksToDoByChildAndNotApproved(@ApiIgnore Principal principal) {
        return taskToDoService.getTasksToDoByChildAndNotApproved(principal);
    }

    @GetMapping
    public List<TaskToDoView> getTasksToDoByChildAndNotApproved(@RequestParam long childId) {
        return taskToDoService.getTasksToDoByChildAndNotApproved(childId);
    }

    @PostMapping
    public void addTaskToDo(@RequestParam long childId, @RequestParam long taskId, @RequestBody TaskToDoData taskToDoData) {
        taskToDoService.addTaskToDo(childId, taskId, taskToDoData);
    }

    @PutMapping("/{taskToDoId}")
    public void updateTaskToDo(@PathVariable long taskToDoId, @RequestBody TaskToDoData taskToDoData) {
        taskToDoService.updateTaskToDo(taskToDoId, taskToDoData);
    }

    @DeleteMapping("/{taskToDoId}")
    public void deleteTaskToDo(@PathVariable long taskToDoId) {
        taskToDoService.deleteTaskToDo(taskToDoId);
    }

    @PutMapping("/{taskToDoId}/done")
    public void setDone(@PathVariable long taskToDoId) {
        taskToDoService.setDone(taskToDoId);
    }

    @PutMapping("/{taskToDoId}/approved")
    public void setApproved(@PathVariable long taskToDoId) {
        taskToDoService.setApproved(taskToDoId);
    }
}