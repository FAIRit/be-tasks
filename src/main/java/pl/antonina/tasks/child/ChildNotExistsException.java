package pl.antonina.tasks.child;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChildNotExistsException extends RuntimeException {
    public ChildNotExistsException(String message) {
        super(message);
    }
}