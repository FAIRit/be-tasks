package pl.antonina.tasks.reward;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class RewardNotExistsException extends RuntimeException {
    public RewardNotExistsException(String message) {
        super(message);
    }
}