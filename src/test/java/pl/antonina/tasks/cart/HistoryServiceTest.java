package pl.antonina.tasks.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private HistoryMapper historyMapper;
    @Mock
    private ChildRepository childRepository;

    private HistoryService historyService;

    @Captor
    ArgumentCaptor<History> historyArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        historyService = new HistoryService(historyRepository, historyMapper, childRepository);
    }

    @Test
    void getByChildId() {
        long childId = 123;
        History history = new History();
        List<History> historyList = List.of(history);
        HistoryView historyView = new HistoryView();
        List<HistoryView> historyViewList = List.of(historyView);

        when(historyRepository.findByChildId(childId)).thenReturn(historyList);
        when(historyMapper.mapHistoryView(history)).thenReturn(historyView);

        List<HistoryView> historyViewListResult = historyService.getByChildId(childId);

        assertThat(historyViewListResult).isEqualTo(historyViewList);
    }

    @Test
    void addHistory() {
        long childId = 123;
        String message = "Zapłaciłam 10 zł";
        Instant modificationDate = Instant.now();
        Integer quantity = 10;

        HistoryData historyData = new HistoryData();
        historyData.setMessage(message);
        historyData.setModificationDate(modificationDate);
        historyData.setQuantity(quantity);

        Child child = new Child();

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        historyService.addHistory(childId, historyData);

        verify(historyRepository).save(historyArgumentCaptor.capture());
        History historyCaptured = historyArgumentCaptor.getValue();

        assertThat(historyCaptured.getChild()).isEqualTo(child);
        assertThat(historyCaptured.getMessage()).isEqualTo(message);
        assertThat(historyCaptured.getModificationDate()).isEqualTo(modificationDate);
        assertThat(historyCaptured.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void deleteHistory() {
        long id = 123;
        historyService.deleteHistory(id);
        verify(historyRepository).deleteById(id);
    }
}