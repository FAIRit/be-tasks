package pl.antonina.tasks.security;

class UserNotExistsException extends RuntimeException {

    public UserNotExistsException(String message) {
        super(message);
    }
}