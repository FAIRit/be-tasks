package pl.antonina.tasks.cart;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.reward.Reward;
import pl.antonina.tasks.taskToDo.TaskToDo;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;

    public HistoryService(HistoryRepository historyRepository, HistoryMapper historyMapper) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
    }

    List<HistoryView> getByChildId(long childId) {
        List<History> historyList = historyRepository.findByChildId(childId);
        return historyList.stream()
                .map(historyMapper::mapHistoryView)
                .collect(Collectors.toList());
    }

    public void addHistory(TaskToDo taskToDo) {
        History history = new History();
        history.setQuantity(taskToDo.getTask().getPoints());
        history.setModificationDate(Instant.now());
        history.setMessage("Approved: " + taskToDo.getTask().getName() + " - " + taskToDo.getTask().getDescription());
        history.setChild(taskToDo.getChild());
        historyRepository.save(history);
    }

    public void addHistory(Reward reward){
        History history = new History();
        history.setQuantity(reward.getPoints() * -1);
        history.setModificationDate(Instant.now());
        history.setMessage("Bought reward: " + reward.getName());
        history.setChild(reward.getChild());
        historyRepository.save(history);
    }

    void deleteHistory(long historyId) {
        historyRepository.deleteById(historyId);
    }
}