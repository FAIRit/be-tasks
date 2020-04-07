package pl.antonina.tasks.taskToDo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class TaskToDoNotExistsException extends RuntimeException {

    public TaskToDoNotExistsException(String message) {
        super(message);
    }
}