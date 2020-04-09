package pl.antonina.tasks.history;


import lombok.Getter;
import lombok.Setter;
import pl.antonina.tasks.child.Child;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="history")
@Getter
@Setter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_gen")
    @SequenceGenerator(name = "history_gen", sequenceName = "history_sequence", allocationSize = 1)
    private Long id;
    @ManyToOne
    private Child child;
    private Instant modificationDate;
    private Integer quantity;
    private String message;
}