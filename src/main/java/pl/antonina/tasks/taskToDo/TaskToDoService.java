package pl.antonina.tasks.taskToDo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.cart.HistoryService;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildNotExistsException;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.task.TaskNotExistsException;
import pl.antonina.tasks.task.TaskRepository;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class TaskToDoService {

    private final TaskToDoRepository taskToDoRepository;
    private final TaskRepository taskRepository;
    private final ChildRepository childRepository;
    private final TaskToDoMapper taskToDoMapper;
    private final HistoryService historyService;
    private final LoggedUserService loggedUserService;

    public TaskToDoService(TaskToDoRepository taskToDoRepository,
                           TaskRepository taskRepository,
                           ChildRepository childRepository,
                           TaskToDoMapper taskToDoMapper,
                           HistoryService historyService,
                           LoggedUserService loggedUserService) {
        this.taskToDoRepository = taskToDoRepository;
        this.taskRepository = taskRepository;
        this.childRepository = childRepository;
        this.taskToDoMapper = taskToDoMapper;
        this.historyService = historyService;
        this.loggedUserService = loggedUserService;
    }

    List<TaskToDoView> getTasksToDoByChildAndNotApproved(Long childId, Principal childPrincipal) {
        long childIdNotNull = Optional.ofNullable(childId).orElseGet(() ->
                loggedUserService.getChild(childPrincipal).getId()
        );
        List<TaskToDo> taskToDoList = taskToDoRepository.findByChildIdAndApprovedOrderByExpectedDateDesc(childIdNotNull, false);
        return taskToDoList.stream()
                .map(taskToDoMapper::mapTaskToDoView)
                .collect(Collectors.toList());
    }

    void addTaskToDo(long childId, long taskId, TaskToDoData taskToDoData) {
        Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotExistsException("Child with given id doesn't exist."));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotExistsException("Task with given id doesn't exist."));
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        taskToDo.setTask(task);
        taskToDo.setChild(child);
        taskToDo.setStartDate(Instant.now());
        taskToDoRepository.save(taskToDo);
    }

    void updateTaskToDo(long taskToDoId, TaskToDoData taskToDoData) {
        TaskToDo taskToDo = taskToDoRepository.findById(taskToDoId).orElseThrow(() -> new TaskToDoNotExistsException("TaskToDo with given id doesn't exist."));
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        taskToDoRepository.save(taskToDo);
    }

    void deleteTaskToDo(long taskToDoId) {
        taskToDoRepository.deleteById(taskToDoId);
    }

    void setDone(long taskToDoId) {
        TaskToDo taskToDo = taskToDoRepository.findById(taskToDoId).orElseThrow(() -> new TaskToDoNotExistsException("TaskToDo with given id doesn't exist."));
        taskToDo.setFinishDate(Instant.now());
        taskToDo.setDone(true);
        taskToDoRepository.save(taskToDo);
    }

    @Transactional
    void setApproved(long taskToDoId) {
        TaskToDo taskToDo = taskToDoRepository.findById(taskToDoId).orElseThrow(() -> new TaskToDoNotExistsException("TaskToDo with given id doesn't exist."));
        taskToDo.setApproved(true);
        taskToDoRepository.save(taskToDo);

        historyService.addHistory(taskToDo);

        Child child = childRepository.findById(taskToDo.getChild().getId()).orElseThrow(() -> new ChildNotExistsException("Child with given id doesn't exist."));
        Integer newPoints = child.getPoints() + taskToDo.getTask().getPoints();
        child.setPoints(newPoints);
        childRepository.save(child);
    }
}