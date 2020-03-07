package pl.antonina.tasks.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper userMapper = new UserMapper();

    @Test
    void mapUserView() {
        final UserType userType = UserType.PARENT;
        final String email = "test@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setType(userType);


        UserView userViewResult = userMapper.mapUserView(user);

        assertThat(userViewResult.getType()).isEqualTo(userType);
        assertThat(userViewResult.getEmail()).isEqualTo(email);
    }
}