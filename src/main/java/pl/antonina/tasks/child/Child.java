package pl.antonina.tasks.child;


import lombok.Data;
import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.parent.Parent;
import pl.antonina.tasks.user.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="children")
@Data
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "children_gen")
    @SequenceGenerator(name = "children_gen", sequenceName = "children_sequence", allocationSize = 1)
    private Long id;
    @ManyToOne
    private Parent parent;
    private String name;
    private Integer points;
    private Gender gender;
    private Instant birthDate;
    @OneToOne
    private User user;
}
