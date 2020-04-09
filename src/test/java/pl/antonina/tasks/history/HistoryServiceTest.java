package pl.antonina.tasks.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.reward.Reward;
import pl.antonina.tasks.security.LoggedUserService;
import pl.antonina.tasks.task.Task;
import pl.antonina.tasks.taskToDo.TaskToDo;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private HistoryMapper historyMapper;
    @Mock
    private LoggedUserService loggedUserService;

    private HistoryService historyService;

    @Captor
    ArgumentCaptor<History> historyArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        historyService = new HistoryServiceImpl(historyRepository, historyMapper, loggedUserService);
    }

    @Test
    void getByChildId() {
        long childId = 123;
        History history = new History();
        List<History> historyList = List.of(history);
        HistoryView historyView = new HistoryView();
        final List<HistoryView> historyViewList = List.of(historyView);

        Parent parent = new Parent();
        long parentId = 345;
        parent.setId(parentId);
        Principal parentPrincipal = mock(Principal.class);
        when(loggedUserService.getParent(parentPrincipal)).thenReturn(parent);
        when(historyRepository.findByChildIdAndChildParentIdOrderByModificationDateDesc(childId, parentId)).thenReturn(historyList);
        when(historyMapper.mapHistoryView(history)).thenReturn(historyView);

        List<HistoryView> historyViewListResult = historyService.getByChildId(parentPrincipal, childId);

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
}