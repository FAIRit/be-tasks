package pl.antonina.tasks.taskToDo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.cart.HistoryService;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.task.TaskRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
class TaskToDoService {

    private final TaskToDoRepository taskToDoRepository;
    private final TaskRepository taskRepository;
    private final ChildRepository childRepository;
    private final TaskToDoMapper taskToDoMapper;
    private final HistoryService historyService;

    public TaskToDoService(TaskToDoRepository taskToDoRepository,
                           TaskRepository taskRepository,
                           ChildRepository childRepository,
                           TaskToDoMapper taskToDoMapper,
                           HistoryService historyService) {
        this.taskToDoRepository = taskToDoRepository;
        this.taskRepository = taskRepository;
        this.childRepository = childRepository;
        this.taskToDoMapper = taskToDoMapper;
        this.historyService = historyService;
    }

    TaskToDoView getTaskToDo(long id) {
        TaskToDo taskToDo = taskToDoRepository.findById(id).orElseThrow();
        return taskToDoMapper.mapTaskToDoView(taskToDo);
    }

    List<TaskToDoView> getTasksToDo(long childId, boolean done, boolean approved) {
        List<TaskToDo> taskToDoList = taskToDoRepository.findByChildIdAndDoneAndApprovedOrderByExpectedDateDesc(childId, done, approved);
        return taskToDoList.stream()
                .map(taskToDoMapper::mapTaskToDoView)
                .collect(Collectors.toList());
    }

    void addTaskToDo(long childId, long taskId, TaskToDoData taskToDoData) {
        Child child = childRepository.findById(childId).orElseThrow();
        Task task = taskRepository.findById(taskId).orElseThrow();
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        taskToDo.setTask(task);
        taskToDo.setChild(child);
        taskToDo.setStartDate(Instant.now());
        taskToDoRepository.save(taskToDo);
    }

    void updateTaskToDo(long id, TaskToDoData taskToDoData) {
        TaskToDo taskToDo = taskToDoRepository.findById(id).orElseThrow();
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        taskToDoRepository.save(taskToDo);
    }

    void deleteTaskToDo(long id) {
        taskToDoRepository.deleteById(id);
    }

    void setDone(long id) {
        TaskToDo taskToDo = taskToDoRepository.findById(id).orElseThrow();
        taskToDo.setDone(true);
        taskToDoRepository.save(taskToDo);
    }

    @Transactional
    void setApproved(long id) {
        TaskToDo taskToDo = taskToDoRepository.findById(id).orElseThrow();
        taskToDo.setApproved(true);
        taskToDoRepository.save(taskToDo);

        historyService.addHistory(taskToDo);

        Child child = childRepository.findById(taskToDo.getChild().getId()).orElseThrow();
        Integer newPoints = child.getPoints() + taskToDo.getTask().getPoints();
        child.setPoints(newPoints);
        childRepository.save(child);
    }
}