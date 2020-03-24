package pl.antonina.tasks.cart;

import pl.antonina.tasks.reward.Reward;
import pl.antonina.tasks.taskToDo.TaskToDo;

import java.util.List;

public interface HistoryService {

    List<HistoryView> getByChildId(long childId);

    void addHistory(TaskToDo taskToDo);

    void addHistory(Reward reward);

    void deleteHistory(long historyId);
}