package pl.antonina.tasks.task;

import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    public Task getTask(@ApiIgnore Principal parentPrincipal, @PathVariable long taskId) {
        return taskService.getTask(parentPrincipal, taskId);
    }

    @GetMapping
    public List<TaskView> getTasksByParent(@ApiIgnore Principal parentPrincipal) {
        return taskService.getTasksByParent(parentPrincipal);
    }

    @PostMapping
    public ResponseEntity<Void> addTask(@ApiIgnore Principal parentPrincipal, @Validated @RequestBody TaskData taskData) {
        long taskId = taskService.addTask(parentPrincipal, taskData);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{taskId}")
                .buildAndExpand(taskId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{taskId}")
    public void updateTask(@ApiIgnore Principal parentPrincipal, @PathVariable long taskId, @Validated @RequestBody TaskData taskData) {
        taskService.updateTask(parentPrincipal, taskId, taskData);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@ApiIgnore Principal parentPrincipal, @PathVariable long taskId) {
        taskService.deleteTask(parentPrincipal, taskId);
    }
}