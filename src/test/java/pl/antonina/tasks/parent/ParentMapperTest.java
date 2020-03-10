package pl.antonina.tasks.parent;

import org.junit.jupiter.api.Test;
import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.User;

import static org.assertj.core.api.Assertions.assertThat;

class ParentMapperTest {

    private final ParentMapper parentMapper = new ParentMapper();

    @Test
    void mapParentView() {
        final String name = "Antonina";
        final String email = "test@gmail.com";
        final Gender parentGender = Gender.FEMALE;
        Parent parent = new Parent();
        User user = new User();
        parent.setUser(user);
        parent.setName(name);
        parent.setGender(parentGender);
        parent.getUser().setEmail(email);

        ParentView parentView = parentMapper.mapParentView(parent);

        assertThat(parentView.getName()).isEqualTo(name);
        assertThat(parentView.getGender()).isEqualTo(parentGender);
        assertThat(parentView.getEmail()).isEqualTo(email);
    }
}