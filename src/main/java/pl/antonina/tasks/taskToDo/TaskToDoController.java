package pl.antonina.tasks.taskToDo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasksToDo")
public class TaskToDoController {

    private final TaskToDoService taskToDoService;

    public TaskToDoController(TaskToDoService taskToDoService) {
        this.taskToDoService = taskToDoService;
    }

    @GetMapping("/{id}")
    public TaskToDoView getTaskToDo(@PathVariable long id) {
        return taskToDoService.getTaskToDo(id);
    }

    @GetMapping
    public List<TaskToDoView> getTasksToDoByChildByDoneByApproved(@RequestParam long childId, @RequestParam boolean done, @RequestParam boolean approved) {
        return taskToDoService.getTasksToDo(childId, done, approved);
    }

    @PostMapping
    public void addTaskToDo(@RequestParam long childId, @RequestParam long taskId, @RequestBody TaskToDoData taskToDoData) {
        taskToDoService.addTaskToDo(childId, taskId, taskToDoData);
    }

    @PutMapping("/{id}")
    public void updateTaskToDo(@PathVariable long id, @RequestBody TaskToDoData taskToDoData) {
        taskToDoService.updateTaskToDo(id, taskToDoData);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskToDo(@PathVariable long id) {
        taskToDoService.deleteTaskToDo(id);
    }

    @PutMapping("/{id}/done")
    public void setDone(@PathVariable long id) {
        taskToDoService.setDone(id);
    }

    @PutMapping("/{id}/approved")
    public void setApproved(@PathVariable long id) {
        taskToDoService.setApproved(id);
    }
}
