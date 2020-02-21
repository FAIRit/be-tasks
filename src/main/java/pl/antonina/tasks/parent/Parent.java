package pl.antonina.tasks.parent;

import lombok.Data;
import pl.antonina.tasks.Gender;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="parents")
@Data
public class Parent {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Gender gender;
}
