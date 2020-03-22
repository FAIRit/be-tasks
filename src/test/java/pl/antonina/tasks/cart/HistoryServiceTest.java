package pl.antonina.tasks.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.reward.Reward;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.taskToDo.TaskToDo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private HistoryMapper historyMapper;

    private HistoryService historyService;

    @Captor
    ArgumentCaptor<History> historyArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        historyService = new HistoryService(historyRepository, historyMapper);
    }

    @Test
    void getByChildId() {
        long childId = 123;
        History history = new History();
        List<History> historyList = List.of(history);
        HistoryView historyView = new HistoryView();
        final List<HistoryView> historyViewList = List.of(historyView);

        when(historyRepository.findByChildId(childId)).thenReturn(historyList);
        when(historyMapper.mapHistoryView(history)).thenReturn(historyView);

        List<HistoryView> historyViewListResult = historyService.getByChildId(childId);

        assertThat(historyViewListResult).isEqualTo(historyViewList);
    }

    @Test
    void addHistoryTaskToDo() {
        String name = "name";
        final Integer points = 123;
        String description = "description";
        final Child child = new Child();

        Task task = new Task();
        task.setDescription(description);
        task.setName(name);
        task.setPoints(points);

        TaskToDo taskToDo = new TaskToDo();
        taskToDo.setTask(task);
        taskToDo.setChild(child);

        final String message = "Approved: " + taskToDo.getTask().getName() + " - " + taskToDo.getTask().getDescription();

        historyService.addHistory(taskToDo);

        verify(historyRepository).save(historyArgumentCaptor.capture());
        History historyCaptured = historyArgumentCaptor.getValue();

        assertThat(historyCaptured.getChild()).isEqualTo(child);
        assertThat(historyCaptured.getMessage()).isEqualTo(message);
        assertThat(historyCaptured.getQuantity()).isEqualTo(points);
    }

    @Test
    void addHistoryReward() {
        String name = "name";
        final Integer points = 123;
        final Child child = new Child();

        Reward reward = new Reward();
        reward.setPoints(points * -1);
        reward.setName(name);
        reward.setChild(child);

        final String message = "Bought reward: " + reward.getName();

        historyService.addHistory(reward);

        verify(historyRepository).save(historyArgumentCaptor.capture());
        History historyCaptured = historyArgumentCaptor.getValue();

        assertThat(historyCaptured.getChild()).isEqualTo(child);
        assertThat(historyCaptured.getMessage()).isEqualTo(message);
        assertThat(historyCaptured.getQuantity()).isEqualTo(points);
    }

    @Test
    void deleteHistory() {
        final long historyId = 123;
        historyService.deleteHistory(historyId);
        verify(historyRepository).deleteById(historyId);
    }
}