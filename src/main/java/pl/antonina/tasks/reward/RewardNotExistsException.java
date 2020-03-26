package pl.antonina.tasks.reward;

class RewardNotExistsException extends RuntimeException {
    public RewardNotExistsException(String message) {
        super(message);
    }
}