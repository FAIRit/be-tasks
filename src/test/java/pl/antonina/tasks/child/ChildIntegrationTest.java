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
        ResponseEntity<Void> responsePostEntity = childTestSupport.postChild(parentUserData, childData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertChild(childData);
        assertUser(childData.getUserData());
    }

    @Test
    void getChild() {
        ResponseEntity<Void> responsePostEntity = childTestSupport.postChild(parentUserData, childData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<ChildView> responseGetEntity = childTestSupport.getChildByParent(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ChildView childView = responseGetEntity.getBody();
        assertThat(childView.getName()).isEqualTo(childData.getName());
        assertThat(childView.getBirthDate()).isEqualTo(childData.getBirthDate());
        assertThat(childView.getGender()).isEqualTo(childData.getGender());
    }

    @Test
    void getChildren() {
        ResponseEntity<Void> responsePostEntity = childTestSupport.postChild(parentUserData, childData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<List<ChildView>> responseGetEntity = childTestSupport.getChildrenByParent(parentUserData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<ChildView> childViewList = responseGetEntity.getBody();
        ChildView childView = childViewList.get(0);
        assertThat(childView.getName()).isEqualTo(childData.getName());
        assertThat(childView.getBirthDate()).isEqualTo(childData.getBirthDate());
        assertThat(childView.getGender()).isEqualTo(childData.getGender());
    }

    @Test
    void updateChild() {
        ResponseEntity<Void> responsePostEntity = childTestSupport.postChild(parentUserData, childData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<Void> responsePutEntity = childTestSupport.putChild(parentUserData, location, newChildData);
        assertThat(responsePutEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertUser(newChildData.getUserData());
        assertChild(newChildData);
    }

    @Test
    void deleteChild() {
        ResponseEntity<Void> responsePostEntity = childTestSupport.postChild(parentUserData, childData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<Void> responseDeleteEntity = childTestSupport.deleteChild(parentUserData, location);
        assertThat(responseDeleteEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<ChildView> responseGetDeletedEntity = childTestSupport.getChildByChild(childData.getUserData());
        assertThat(responseGetDeletedEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private void assertUser(UserData userData) {
        ResponseEntity<UserView> responseGetEntity = childTestSupport.getUser(userData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserView actualUser = responseGetEntity.getBody();
        assertThat(actualUser.getType()).isEqualTo(UserType.CHILD);
        assertThat(actualUser.getEmail()).isEqualTo(userData.getEmail());
    }

    private void assertChild(ChildData childData) {
        ResponseEntity<ChildView> responseGetEntity = childTestSupport.getChildByChild(childData.getUserData());
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ChildView actualChild = responseGetEntity.getBody();
        assertThat(actualChild.getPoints()).isEqualTo(0);
        assertThat(actualChild.getBirthDate()).isEqualTo(childData.getBirthDate());
        assertThat(actualChild.getGender()).isEqualTo(childData.getGender());
        assertThat(actualChild.getName()).isEqualTo(childData.getName());
        assertThat(actualChild.getEmail()).isEqualTo(childData.getUserData().getEmail());
    }
}