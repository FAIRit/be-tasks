package pl.antonina.tasks.taskToDo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskToDoService {

    private TaskToDoRepository taskToDoRepository;

    public TaskToDoService(TaskToDoRepository taskToDoRepository) {
        this.taskToDoRepository = taskToDoRepository;
    }

    public List<TaskToDo> getTasksToDoByChildByDoneByApproved(Long childId, boolean done, boolean approved) {
        return taskToDoRepository.findByChildIdAndDoneAndApprovedOrderByExpectedDateDesc(childId, done, approved);
    }
}
