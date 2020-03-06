package pl.antonina.tasks.taskToDo;

class TaskToDoNotExistsException extends RuntimeException {

    public TaskToDoNotExistsException(String message) {
        super(message);
    }
}