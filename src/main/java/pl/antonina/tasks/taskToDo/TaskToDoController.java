package pl.antonina.tasks.taskToDo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/parents/{parentId}/children/{childId}")
public class TaskToDoController {

    private TaskToDoService taskToDoService;

    public TaskToDoController(TaskToDoService taskToDoService) {
        this.taskToDoService = taskToDoService;
    }

    @GetMapping
    public List<TaskToDo> getTasksToDoByChildByDoneByApproved(@PathVariable Long childId,
                                                              @RequestParam (required = false) boolean done,
                                                              @RequestParam (required = false) boolean approved){
        return taskToDoService.getTasksToDoByChildByDoneByApproved(childId, done, approved);
    }
}
