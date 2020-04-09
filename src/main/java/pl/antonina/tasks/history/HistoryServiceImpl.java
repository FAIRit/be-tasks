package pl.antonina.tasks.history;

import org.springframework.stereotype.Service;
import pl.antonina.tasks.reward.Reward;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.taskToDo.TaskToDo;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    private final LoggedUserService loggedUserService;

    public HistoryServiceImpl(HistoryRepository historyRepository, HistoryMapper historyMapper, LoggedUserService loggedUserService) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
        this.loggedUserService = loggedUserService;
    }

    @Override
    public List<HistoryView> getByChildId(Principal parentPrincipal, long childId) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        List<History> historyList = historyRepository.findByChildIdAndChildParentIdOrderByModificationDateDesc(childId, parentId);
        return historyList.stream()
                .map(historyMapper::mapHistoryView)
                .collect(Collectors.toList());
    }

    @Override
    public void addHistory(TaskToDo taskToDo) {
        History history = new History();
        history.setQuantity(taskToDo.getTask().getPoints());
        history.setModificationDate(Instant.now());
        history.setMessage("Approved: " + taskToDo.getTask().getName() + " - " + taskToDo.getTask().getDescription());
        history.setChild(taskToDo.getChild());
        historyRepository.save(history);
    }

    @Override
    public void addHistory(Reward reward){
        History history = new History();
        history.setQuantity(reward.getPoints() * -1);
        history.setModificationDate(Instant.now());
        history.setMessage("Bought reward: " + reward.getName());
        history.setChild(reward.getChild());
        historyRepository.save(history);
    }
}