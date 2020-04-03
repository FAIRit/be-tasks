package pl.antonina.tasks.parent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.user.UserData;
import pl.antonina.tasks.user.UserType;
import pl.antonina.tasks.user.UserView;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int serverPort;

    private ParentData parentData;
    private ParentData newParentData;
    private ParentTestSupport parentTestSupport;

    @BeforeEach
    void beforeEach() {
        parentData = ParentCreator.createParentData();
        newParentData = ParentCreator.createNewParentData();
        parentTestSupport = new ParentTestSupport(restTemplate, serverPort);
    }

    @Test
    void addParent() {
        ResponseEntity<Void> responsePostEntity = parentTestSupport.postParent(parentData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertParent(parentData);
        assertUser(parentData.getUserData());
    }

    @Test
    void updateParent() {
        ResponseEntity<Void> responsePostEntity = parentTestSupport.postParent(parentData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> responsePutEntity = parentTestSupport.putParent(parentData.getUserData(), newParentData);
        assertThat(responsePutEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertParent(newParentData);
        assertUser(newParentData.getUserData());
    }

    @Test
    void deleteParent() {
        ResponseEntity<Void> responseEntity = parentTestSupport.postParent(parentData);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> responseDeleteEntity = parentTestSupport.deleteParent(parentData.getUserData());
        assertThat(responseDeleteEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<ParentView> parentResponseEntity = parentTestSupport.getParent(parentData.getUserData());
        assertThat(parentResponseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private void assertParent(ParentData parentData) {
        ResponseEntity<ParentView> responseGetEntity = parentTestSupport.getParent(parentData.getUserData());
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ParentView actualParent = responseGetEntity.getBody();
        assertThat(actualParent.getName()).isEqualTo(parentData.getName());
        assertThat(actualParent.getGender()).isEqualTo(parentData.getGender());
        assertThat(actualParent.getEmail()).isEqualTo(parentData.getUserData().getEmail());
    }

    private void assertUser(UserData userData) {
        ResponseEntity<UserView> responseGetEntity = parentTestSupport.getUser(userData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserView actualUser = responseGetEntity.getBody();
        assertThat(actualUser.getType()).isEqualTo(UserType.PARENT);
        assertThat(actualUser.getEmail()).isEqualTo(userData.getEmail());
    }
}