package pl.antonina.tasks.taskToDo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/parents/{parentId}/children/{childId}/tasksToDo")
public class TaskToDoController {

    private final TaskToDoService taskToDoService;

    public TaskToDoController(TaskToDoService taskToDoService) {
        this.taskToDoService = taskToDoService;
    }

    @GetMapping("/{id}")
    public TaskToDoView getTaskToDo(@PathVariable long parentId, @PathVariable long childId, @PathVariable long id){
        return taskToDoService.getTaskToDo(id);
    }

    @GetMapping
    public List<TaskToDoView> getTasksToDoByChildByDoneByApproved(@PathVariable long parentId, @PathVariable long childId,
                                                              @RequestParam boolean done, @RequestParam boolean approved) {
        return taskToDoService.getTasksToDoByChildByDoneByApproved(childId, done, approved);
    }

    @PostMapping
    public void addTaskToDo(@PathVariable long parentId, @PathVariable long childId,
                            @RequestParam long taskId, @RequestBody TaskToDoData taskToDoData) {
        taskToDoService.addTaskToDo(childId, taskId, taskToDoData);
    }

    @PutMapping("/{id}")
    public void updateTaskToDo(@PathVariable long parentId, @PathVariable long childId,
                               @PathVariable long id, @RequestBody TaskToDoData taskToDoData){
        taskToDoService.updateTaskToDo(id, taskToDoData);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskToDo(@PathVariable long parentId, @PathVariable long childId, @PathVariable long id){
        taskToDoService.deleteTaskToDo(id);
    }
}
