package pl.antonina.tasks.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildMapper;
import pl.antonina.tasks.child.ChildView;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryMapperTest {

    @Mock
    private ChildMapper childMapper;

    private HistoryMapper historyMapper;

    @BeforeEach
    void beforeEach() {
        historyMapper = new HistoryMapper(childMapper);
    }

    @Test
    void mapHistoryView() {
        History history = new History();
        String message = "Message";
        Instant date = Instant.now();
        Integer quantity = 5;
        history.setModificationDate(date);
        history.setMessage(message);
        history.setQuantity(quantity);

        Child child = new Child();
        history.setChild(child);

        ChildView childView = mock(ChildView.class);
        when(childMapper.mapChildView(child)).thenReturn(childView);

        HistoryView historyView = historyMapper.mapHistoryView(history);

        assertThat(historyView.getMessage()).isEqualTo(message);
        assertThat(historyView.getModificationDate()).isEqualTo(date);
        assertThat(historyView.getQuantity()).isEqualTo(quantity);
        assertThat(historyView.getChildView()).isEqualTo(childView);
    }
}