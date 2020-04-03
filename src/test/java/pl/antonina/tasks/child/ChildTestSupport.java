package pl.antonina.tasks.child;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.user.UserData;
import pl.antonina.tasks.user.UserView;

import java.util.List;

public class ChildTestSupport {

    private final TestRestTemplate restTemplate;
    private final int serverPort;

    public ChildTestSupport(TestRestTemplate restTemplate, int serverPort) {
        this.restTemplate = restTemplate;
        this.serverPort = serverPort;

    }

    ResponseEntity<List<ChildView>> getChildrenByParent(UserData parentUserData) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(getChildrenUrl() + "/byParent", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<ChildView>>() {
                });
    }

    ResponseEntity<ChildView> getChildByParent(UserData parentUserData, String location) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .getForEntity(location, ChildView.class);
    }

    ResponseEntity<ChildView> getChildByChild(UserData childUserData) {
        return restTemplate
                .withBasicAuth(childUserData.getEmail(), childUserData.getPassword())
                .getForEntity(getChildrenUrl(), ChildView.class);
    }

    ResponseEntity<Void> postChild(UserData parentUserData, ChildData childData) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .postForEntity(getChildrenUrl(), childData, Void.class);
    }

    ResponseEntity<UserView> getUser(UserData userData) {
        return restTemplate
                .withBasicAuth(userData.getEmail(), userData.getPassword())
                .getForEntity(getUrl("/api/users/validateLogin"), UserView.class);
    }

    ResponseEntity<Void> putChild(UserData parentUserData, String location, ChildData newChildData) {
        HttpEntity<ChildData> requestEntity = new HttpEntity<>(newChildData);
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(location, HttpMethod.PUT, requestEntity, Void.class);
    }

    ResponseEntity<Void> deleteChild(UserData parentUserData, String location) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(location, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
    }

    private String getChildrenUrl() {
        return getUrl("/api/children");
    }

    private String getUrl(String path) {
        return "http://localhost:" + serverPort + path;
    }

    public Pair<UserData, ChildView> createChild(UserData parentUserData) {
        ChildData childData = ChildCreator.createChildData();

        ResponseEntity<Void> responsePostEntity = postChild(parentUserData, childData);
        String location = responsePostEntity.getHeaders().getLocation().toString();

        ResponseEntity<ChildView> responseGetEntity = getChildByParent(parentUserData, location);
        ChildView childView = responseGetEntity.getBody();

        return Pair.of(childData.getUserData(), childView);
    }
}