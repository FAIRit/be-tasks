package pl.antonina.tasks.cart;


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
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Child child;
    private Instant modificationDate;
    private Integer quantity;
    private String message;
}
