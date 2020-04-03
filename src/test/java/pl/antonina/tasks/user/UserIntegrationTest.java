package pl.antonina.tasks.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.child.ChildTestSupport;
import pl.antonina.tasks.parent.ParentTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int serverPort;

    private UserData parentUserData;

    @BeforeEach
    void beforeEach() {
        ParentTestSupport parentTestSupport = new ParentTestSupport(restTemplate, serverPort);
        parentUserData = parentTestSupport.createParent();
    }

    @Test
    void parentTest() {
        assertUser(parentUserData, UserType.PARENT);
    }

    @Test
    void childTest() {
        ChildTestSupport childTestSupport = new ChildTestSupport(restTemplate, serverPort);
        UserData childUserData = childTestSupport.createChild(parentUserData).getFirst();
        assertUser(childUserData, UserType.CHILD);
    }

    private void assertUser(UserData userData, UserType userType) {
        ResponseEntity<UserView> responseGetEntity = getUser(userData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserView actualUser = responseGetEntity.getBody();

        assertThat(actualUser.getType()).isEqualTo(userType);
        assertThat(actualUser.getEmail()).isEqualTo(userData.getEmail());
    }

    private ResponseEntity<UserView> getUser(UserData userData) {
        return restTemplate
                .withBasicAuth(userData.getEmail(), userData.getPassword())
                .getForEntity(getUrl(), UserView.class);
    }

    private String getUrl() {
        return "http://localhost:" + serverPort + "/api/users/validateLogin";
    }
}