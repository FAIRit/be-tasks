package pl.antonina.tasks.taskToDo;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.child.ChildView;
import pl.antonina.tasks.task.TaskView;
import pl.antonina.tasks.user.UserData;

import java.util.List;

class TaskToDoTestSupport {

    private final TestRestTemplate restTemplate;
    private final int serverPort;

    TaskToDoTestSupport(TestRestTemplate restTemplate, int serverPort) {
        this.restTemplate = restTemplate;
        this.serverPort = serverPort;
    }

    ResponseEntity<List<TaskToDoView>> getTasksToDoByChild(UserData childUserData) {
        return restTemplate
                .withBasicAuth(childUserData.getEmail(), childUserData.getPassword())
                .exchange(getTasksToDoUrl() + "/byChild", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<TaskToDoView>>() {
                });
    }

    ResponseEntity<List<TaskToDoView>> getTasksToDoByParent(UserData parentUserData, ChildView childView) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(getTasksToDoUrl() + "?childId=" + childView.getId(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<TaskToDoView>>() {
                });
    }

    ResponseEntity<TaskToDoView> getTaskToDo(UserData parentUserData, String location) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .getForEntity(location, TaskToDoView.class);
    }

    ResponseEntity<Void> postTaskToDo(UserData parentUserData, ChildView childView, TaskView taskView, TaskToDoData taskToDoData) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .postForEntity(getTasksToDoUrl() + "?childId=" + childView.getId() + "&taskId=" + taskView.getId(), taskToDoData, Void.class);
    }

    ResponseEntity<Void> deleteTaskToDo(UserData parentUserData, String location) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(location, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
    }

    ResponseEntity<Void> putTaskToDo(UserData parentUserData, String location, TaskToDoData newTaskToDoData) {
        HttpEntity<TaskToDoData> requestEntity = new HttpEntity<>(newTaskToDoData);
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(location, HttpMethod.PUT, requestEntity, Void.class);
    }

    ResponseEntity<Void> setDoneTaskToDo(UserData childUserData, String location) {
        return restTemplate
                .withBasicAuth(childUserData.getEmail(), childUserData.getPassword())
                .exchange(location + "/done", HttpMethod.PUT, HttpEntity.EMPTY, Void.class);
    }

    ResponseEntity<Void> setApprovedTaskToDo(UserData parentUserData, String location) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(location + "/approved", HttpMethod.PUT, HttpEntity.EMPTY, Void.class);
    }

    private String getTasksToDoUrl() {
        return "http://localhost:" + serverPort + "/api/tasksToDo";
    }
}