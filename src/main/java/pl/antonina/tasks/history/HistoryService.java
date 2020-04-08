package pl.antonina.tasks.history;

import pl.antonina.tasks.reward.Reward;
import pl.antonina.tasks.taskToDo.TaskToDo;

import java.security.Principal;
import java.util.List;

public interface HistoryService {

    List<HistoryView> getByChildId(Principal parentPrincipal, long childId);

    void addHistory(TaskToDo taskToDo);

    void addHistory(Reward reward);
}