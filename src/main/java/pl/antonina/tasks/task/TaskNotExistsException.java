package pl.antonina.tasks.task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotExistsException extends RuntimeException {

    public TaskNotExistsException(String message) {
        super(message);
    }
}