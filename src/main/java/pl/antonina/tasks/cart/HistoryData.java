package pl.antonina.tasks.cart;

import lombok.Data;
import pl.antonina.tasks.child.Child;

import java.time.Instant;

@Data
public class HistoryData {

    private Instant modificationDate;
    private Integer quantity;
    private String message;
}
