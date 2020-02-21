package pl.antonina.tasks.taskToDo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/parents/{parentId}/children/{childId}/tasksToDo")
public class TaskToDoController {

    private TaskToDoService taskToDoService;

    public TaskToDoController(TaskToDoService taskToDoService) {
        this.taskToDoService = taskToDoService;
    }

    @GetMapping("/{id}")
    public TaskToDo getTaskToDo(@PathVariable Long parentId, @PathVariable Long childId, @PathVariable Long id){
        return taskToDoService.getTaskToDo(id);
    }

    @GetMapping
    public List<TaskToDo> getTasksToDoByChildByDoneByApproved(@PathVariable Long parentId, @PathVariable Long childId,
                                                              @RequestParam boolean done, @RequestParam boolean approved) {
        return taskToDoService.getTasksToDoByChildByDoneByApproved(childId, done, approved);
    }

    @PostMapping
    public void addTaskToDo(@PathVariable Long parentId, @PathVariable Long childId,
                            @RequestParam Long taskId, @RequestBody TaskToDoData taskToDoData) {
        taskToDoService.addTaskToDo(childId, taskId, taskToDoData);
    }

    @PutMapping("/{id}")
    public void updateTaskToDo(@PathVariable Long parentId, @PathVariable Long childId,
                               @PathVariable Long id, @RequestBody TaskToDoData taskToDoData){
        taskToDoService.updateTaskToDo(id, taskToDoData);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskToDo(@PathVariable Long parentId, @PathVariable Long childId, @PathVariable Long id){
        taskToDoService.deleteTaskToDo(id);
    }
}
