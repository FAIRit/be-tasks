package pl.antonina.tasks.user;

class UserAlreadyExistsException extends RuntimeException {

    UserAlreadyExistsException(String message) {
        super(message);
    }
}