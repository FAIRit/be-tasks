package pl.antonina.tasks.reward;

import lombok.Getter;
import lombok.Setter;
import pl.antonina.tasks.child.Child;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "reward")
@Getter
@Setter
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reward_gen")
    @SequenceGenerator(name = "reward_gen", sequenceName = "reward_sequence", allocationSize = 1)
    private Long id;
    private String name;
    private String url;
    private Integer points;
    @ManyToOne
    private Child child;
    private boolean bought;
}