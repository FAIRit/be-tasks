package pl.antonina.tasks.child;


import lombok.Data;
import pl.antonina.tasks.parent.Parent;

import javax.persistence.*;

@Entity
@Table(name="children")
@Data
public class Child {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Parent parent;
    private String name;
    private Integer points;
}
