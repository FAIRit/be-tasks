package pl.antonina.tasks.taskToDo;

import org.springframework.validation.annotation.Validated;
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
    public TaskToDoView getTaskToDoById(@ApiIgnore Principal parentPrincipal, @PathVariable long taskToDoId) {
        return taskToDoService.getTaskToDoById(parentPrincipal, taskToDoId);
    }

    @GetMapping("/byChild")
    public List<TaskToDoView> getTasksToDoByChildAndNotApproved(@ApiIgnore Principal childPrincipal) {
        return taskToDoService.getTasksToDoByChildAndNotApproved(childPrincipal);
    }

    @GetMapping
    public List<TaskToDoView> getTasksToDoByChildAndNotApproved(@ApiIgnore Principal parentPrincipal, @RequestParam long childId) {
        return taskToDoService.getTasksToDoByChildAndNotApproved(parentPrincipal, childId);
    }

    @PostMapping
    public void addTaskToDo(@ApiIgnore Principal parentPrincipal, @RequestParam long childId, @RequestParam long taskId, @Validated @RequestBody TaskToDoData taskToDoData) {
        taskToDoService.addTaskToDo(parentPrincipal, childId, taskId, taskToDoData);
    }

    @PutMapping("/{taskToDoId}")
    public void updateTaskToDo(@ApiIgnore Principal parentPrincipal, @PathVariable long taskToDoId, @Validated @RequestBody TaskToDoData taskToDoData) {
        taskToDoService.updateTaskToDo(parentPrincipal, taskToDoId, taskToDoData);
    }

    @DeleteMapping("/{taskToDoId}")
    public void deleteTaskToDo(@ApiIgnore Principal parentPrincipal, @PathVariable long taskToDoId) {
        taskToDoService.deleteTaskToDo(parentPrincipal, taskToDoId);
    }

    @PutMapping("/{taskToDoId}/done")
    public void setDone(@ApiIgnore Principal childPrincipal, @PathVariable long taskToDoId) {
        taskToDoService.setDone(childPrincipal, taskToDoId);
    }

    @PutMapping("/{taskToDoId}/approved")
    public void setApproved(@ApiIgnore Principal parentPrincipal, @PathVariable long taskToDoId) {
        taskToDoService.setApproved(parentPrincipal, taskToDoId);
    }
}