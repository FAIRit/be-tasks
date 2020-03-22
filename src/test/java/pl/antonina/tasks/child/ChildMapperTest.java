package pl.antonina.tasks.child;

import org.junit.jupiter.api.Test;
import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ChildMapperTest {

    private final ChildMapper childMapper = new ChildMapper();

    @Test
    void mapChildView() {
        final long childId = 1;
        final String name = "Natalia";
        final String email = "test@gmail.com";
        final Gender gender = Gender.FEMALE;
        final LocalDate birthDate = LocalDate.of(2019, 2, 16);
        final Integer points = 22;
        Child child = new Child();
        User user = new User();
        child.setId(childId);
        child.setUser(user);
        child.setName(name);
        child.setGender(gender);
        child.setBirthDate(birthDate);
        child.getUser().setEmail(email);
        child.setPoints(points);

        ChildView childView = childMapper.mapChildView(child);

        assertThat(childView.getId()).isEqualTo(childId);
        assertThat(childView.getName()).isEqualTo(name);
        assertThat(childView.getEmail()).isEqualTo(email);
        assertThat(childView.getGender()).isEqualTo(gender);
        assertThat(childView.getBirthDate()).isEqualTo(birthDate);
        assertThat(childView.getPoints()).isEqualTo(points);
    }
}