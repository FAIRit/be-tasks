package pl.antonina.tasks.parent;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.user.UserData;
import pl.antonina.tasks.user.UserView;

public class ParentTestSupport {

    private final TestRestTemplate restTemplate;
    private final int serverPort;

    public ParentTestSupport(TestRestTemplate restTemplate, int serverPort) {
        this.restTemplate = restTemplate;
        this.serverPort = serverPort;
    }

    ResponseEntity<Void> postParent(ParentData parentData) {
        return restTemplate
                .postForEntity(getParentsUrl(), parentData, Void.class);
    }

    ResponseEntity<Void> putParent(UserData userData, ParentData newParentData) {
        HttpEntity<ParentData> requestEntity = new HttpEntity<>(newParentData);
        return restTemplate
                .withBasicAuth(userData.getEmail(), userData.getPassword())
                .exchange(getParentsUrl(), HttpMethod.PUT, requestEntity, Void.class);
    }

    ResponseEntity<ParentView> getParent(UserData userData) {
        return restTemplate
                .withBasicAuth(userData.getEmail(), userData.getPassword())
                .getForEntity(getParentsUrl(), ParentView.class);
    }

    ResponseEntity<Void> deleteParent(UserData userData) {
        return restTemplate
                .withBasicAuth(userData.getEmail(), userData.getPassword())
                .exchange(getParentsUrl(), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
    }

    ResponseEntity<UserView> getUser(UserData userData) {
        return restTemplate
                .withBasicAuth(userData.getEmail(), userData.getPassword())
                .getForEntity(getUrl("/api/users/validateLogin"), UserView.class);
    }

    private String getParentsUrl() {
        return getUrl("/api/parents");
    }

    private String getUrl(String path) {
        return "http://localhost:" + serverPort + path;
    }

    public UserData createParent() {
        ParentData parentData = ParentCreator.createParentData();
        postParent(parentData);
        return parentData.getUserData();
    }
}
