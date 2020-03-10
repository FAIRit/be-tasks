package pl.antonina.tasks.task;

public class TaskNotExistsException extends RuntimeException {

    public TaskNotExistsException(String message) {
        super(message);
    }
}