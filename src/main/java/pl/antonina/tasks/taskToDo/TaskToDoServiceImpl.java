package pl.antonina.tasks.taskToDo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.history.HistoryService;
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
import java.util.stream.Collectors;

@Service
class TaskToDoServiceImpl implements TaskToDoService {

    private final TaskToDoRepository taskToDoRepository;
    private final TaskRepository taskRepository;
    private final ChildRepository childRepository;
    private final TaskToDoMapper taskToDoMapper;
    private final HistoryService historyService;
    private final LoggedUserService loggedUserService;

    public TaskToDoServiceImpl(TaskToDoRepository taskToDoRepository,
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

    @Override
    public TaskToDoView getTaskToDoById(Principal parentPrincipal, long taskToDoId) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        TaskToDo taskToDo = taskToDoRepository.findByIdAndTaskParentId(taskToDoId, parentId)
                .orElseThrow(() -> new TaskToDoNotExistsException("TaskToDo with id=" + taskToDoId + " doesn't exist."));
        return taskToDoMapper.mapTaskToDoView(taskToDo);
    }

    @Override
    public List<TaskToDoView> getTasksToDoByChildAndNotApproved(Principal childPrincipal) {
        long childId = loggedUserService.getChild(childPrincipal).getId();
        List<TaskToDo> taskToDoList = taskToDoRepository
                .findByChildIdAndApprovedOrderByExpectedDateDesc(childId, false);
        return taskToDoList.stream()
                .map(taskToDoMapper::mapTaskToDoView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskToDoView> getTasksToDoByChildAndNotApproved(Principal parentPrincipal, long childId) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        List<TaskToDo> taskToDoList = taskToDoRepository
                .findByChildIdAndTaskParentIdAndApprovedOrderByExpectedDateDesc(childId, parentId, false);
        return taskToDoList.stream()
                .map(taskToDoMapper::mapTaskToDoView)
                .collect(Collectors.toList());
    }

    @Override
    public long addTaskToDo(Principal parentPrincipal, long childId, long taskId, TaskToDoData taskToDoData) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        Child child = childRepository.findByIdAndParentId(childId, parentId)
                .orElseThrow(() -> new ChildNotExistsException("Child with id=" + childId + " doesn't exist."));
        Task task = taskRepository.findByIdAndParentId(taskId, parentId)
                .orElseThrow(() -> new TaskNotExistsException("Task with id=" + taskId + " doesn't exist."));
        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        taskToDo.setTask(task);
        taskToDo.setChild(child);
        taskToDo.setStartDate(Instant.now());
        return taskToDoRepository.save(taskToDo).getId();
    }

    @Override
    public void updateTaskToDo(Principal parentPrincipal, long taskToDoId, TaskToDoData taskToDoData) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        TaskToDo taskToDo = taskToDoRepository.findByIdAndTaskParentId(taskToDoId, parentId)
                .orElseThrow(() -> new TaskToDoNotExistsException("TaskToDo with id=" + taskToDoId + " doesn't exist."));
        taskToDo.setExpectedDate(taskToDoData.getExpectedDate());
        taskToDoRepository.save(taskToDo);
    }

    @Override
    public void deleteTaskToDo(Principal parentPrincipal, long taskToDoId) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        TaskToDo taskToDo = taskToDoRepository.findByIdAndTaskParentId(taskToDoId, parentId)
                .orElseThrow(() -> new TaskToDoNotExistsException("TaskToDo with id=" + taskToDoId + " doesn't exist."));
        taskToDoRepository.delete(taskToDo);
    }

    @Override
    public void setDone(Principal childPrincipal, long taskToDoId) {
        long childId = loggedUserService.getChild(childPrincipal).getId();
        TaskToDo taskToDo = taskToDoRepository.findByIdAndChildId(taskToDoId, childId)
                .orElseThrow(() -> new TaskToDoNotExistsException("TaskToDo with id=" + taskToDoId + " doesn't exist."));
        if (!taskToDo.isDone()){
            taskToDo.setFinishDate(Instant.now());
            taskToDo.setDone(true);
            taskToDoRepository.save(taskToDo);
        }
    }

    @Override
    @Transactional
    public void setApproved(Principal parentPrincipal, long taskToDoId) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        TaskToDo taskToDo = taskToDoRepository.findByIdAndTaskParentId(taskToDoId, parentId)
                .orElseThrow(() -> new TaskToDoNotExistsException("TaskToDo with id=" + taskToDoId + " doesn't exist."));
        taskToDo.setApproved(true);
        taskToDoRepository.save(taskToDo);

        historyService.addHistory(taskToDo);

        Child child = childRepository.findByIdAndParentId(taskToDo.getChild().getId(), parentId)
                .orElseThrow(() -> new ChildNotExistsException("Child with id=" + taskToDo.getChild().getId() + " doesn't exist."));
        Integer newPoints = child.getPoints() + taskToDo.getTask().getPoints();
        child.setPoints(newPoints);
        childRepository.save(child);
    }
}