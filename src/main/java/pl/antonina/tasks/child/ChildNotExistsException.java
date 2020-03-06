package pl.antonina.tasks.child;

public class ChildNotExistsException extends RuntimeException {
    public ChildNotExistsException(String message) {
        super(message);
    }
}