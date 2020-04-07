package pl.antonina.tasks.child;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.parent.ParentTestSupport;
import pl.antonina.tasks.user.UserData;
import pl.antonina.tasks.user.UserType;
import pl.antonina.tasks.user.UserView;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChildIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int serverPort;

    private ChildData childData;
    private ChildData newChildData;
    private UserData parentUserData;
    private ChildTestSupport childTestSupport;

    @BeforeEach
    void beforeEach() {
        childData = ChildCreator.createChildData();
        newChildData = ChildCreator.createNewChildData();
        ParentTestSupport parentTestSupport = new ParentTestSupport(restTemplate, serverPort);
        parentUserData = parentTestSupport.createParent();
        childTestSupport = new ChildTestSupport(restTemplate, serverPort);
    }

    @Test
    void addChild() {
        String location = postAndGetLocation(childData);
        ChildView childView = getChildView(location);
        UserView userView = getUserView(childData.getUserData());

        assertUser(childData.getUserData(), userView);
        assertChild(childData, childView);
    }

    @Test
    void getChildByParent() {
        String location = postAndGetLocation(childData);
        ResponseEntity<ChildView> responseGetEntity = childTestSupport.getChildByParent(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ChildView childView = responseGetEntity.getBody();

        assertChild(childData, childView);
    }

    @Test
    void getChildren() {
        String firstLocation = postAndGetLocation(childData);
        ChildView firstChildView = getChildView(firstLocation);
        String secondLocation = postAndGetLocation(newChildData);
        ChildView secondChildView = getChildView(secondLocation);

        ResponseEntity<List<ChildView>> responseGetEntity = childTestSupport.getChildrenByParent(parentUserData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ChildView> childViewList = responseGetEntity.getBody();

        assertThat(childViewList).containsExactlyInAnyOrder(firstChildView, secondChildView);
    }

    @Test
    void updateChild() {
        String location = postAndGetLocation(childData);
        ResponseEntity<Void> responsePutEntity = childTestSupport.putChild(parentUserData, location, newChildData);
        assertThat(responsePutEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserView userView = getUserView(newChildData.getUserData());
        ChildView childView = getChildView(location);

        assertUser(newChildData.getUserData(), userView);
        assertChild(newChildData, childView);
    }

    @Test
    void deleteChild() {
        String location = postAndGetLocation(childData);
        ResponseEntity<Void> responseDeleteEntity = childTestSupport.deleteChild(parentUserData, location);
        assertThat(responseDeleteEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<ChildView> responseGetDeletedEntity = childTestSupport.getChildByChild(childData.getUserData());
        assertThat(responseGetDeletedEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private String postAndGetLocation(ChildData childData) {
        ResponseEntity<Void> responsePostEntity = childTestSupport.postChild(parentUserData, childData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return responsePostEntity.getHeaders().getLocation().toString();
    }

    private ChildView getChildView(String location) {
        ResponseEntity<ChildView> responseGetEntity = childTestSupport.getChildByParent(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseGetEntity.getBody();
    }

    private UserView getUserView(UserData userData) {
        ResponseEntity<UserView> responseGetEntity = childTestSupport.getUser(userData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseGetEntity.getBody();
    }

    private void assertUser(UserData userData, UserView userView) {
        assertThat(userView.getType()).isEqualTo(UserType.CHILD);
        assertThat(userView.getEmail()).isEqualTo(userData.getEmail());
    }

    private void assertChild(ChildData childData, ChildView childView) {
        assertThat(childView.getPoints()).isEqualTo(0);
        assertThat(childView.getBirthDate()).isEqualTo(childData.getBirthDate());
        assertThat(childView.getGender()).isEqualTo(childData.getGender());
        assertThat(childView.getName()).isEqualTo(childData.getName());
        assertThat(childView.getEmail()).isEqualTo(childData.getUserData().getEmail());
    }
}