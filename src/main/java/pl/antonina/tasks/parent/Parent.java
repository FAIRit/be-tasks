package pl.antonina.tasks.parent;

import lombok.Data;
import pl.antonina.tasks.gender.Gender;

import javax.persistence.*;

@Entity
@Table(name="parents")
@Data
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parents_gen")
    @SequenceGenerator(name = "parents_gen", sequenceName = "parents_sequence", allocationSize = 1)
    private Long id;
    private String name;
    private Gender gender;
}
