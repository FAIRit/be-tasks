package pl.antonina.tasks.history;

import lombok.Data;
import pl.antonina.tasks.child.ChildView;

import java.time.Instant;
@Data
public class HistoryView {

    private ChildView childView;
    private Instant modificationDate;
    private Integer quantity;
    private String message;
}
