package pl.antonina.tasks.task;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.user.UserData;

import java.util.List;

public class TaskTestSupport {

    private final TestRestTemplate restTemplate;
    private final int serverPort;

    public TaskTestSupport(TestRestTemplate restTemplate, int serverPort) {
        this.restTemplate = restTemplate;
        this.serverPort = serverPort;
    }

    ResponseEntity<TaskView> getTask(UserData parentUserData, String location) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .getForEntity(location, TaskView.class);
    }

    ResponseEntity<List<TaskView>> getTasks(UserData parentData) {
        return restTemplate
                .withBasicAuth(parentData.getEmail(), parentData.getPassword())
                .exchange(getTasksUrl(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<TaskView>>() {
                });
    }

    ResponseEntity<Void> postTask(UserData parentUserData, TaskData taskData) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .postForEntity(getTasksUrl(), taskData, Void.class);
    }

    ResponseEntity<Void> putTask(String location, UserData parentUserData, TaskData newTaskData) {
        HttpEntity<TaskData> requestEntity = new HttpEntity<>(newTaskData);
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(location, HttpMethod.PUT, requestEntity, Void.class);
    }

    ResponseEntity<Void> deleteTask(String location, UserData parentData) {
        return restTemplate
                .withBasicAuth(parentData.getEmail(), parentData.getPassword())
                .exchange(location, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
    }

    private String getTasksUrl() {
        return "http://localhost:" + serverPort + "api/tasks";
    }

    public Pair<TaskData, TaskView> createTask(UserData parentUserData) {
        TaskData taskData = TaskCreator.createTaskData();

        ResponseEntity<Void> responsePostEntity = postTask(parentUserData, taskData);
        String location = responsePostEntity.getHeaders().getLocation().toString();

        ResponseEntity<TaskView> responseGetEntity = getTask(parentUserData, location);
        TaskView taskView = responseGetEntity.getBody();
        return Pair.of(taskData, taskView);
    }
}