package pl.antonina.tasks.taskToDo;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.child.ChildView;
import pl.antonina.tasks.task.TaskView;
import pl.antonina.tasks.user.UserData;

class TaskToDoTestSupport {

    private final TestRestTemplate restTemplate;
    private final int serverPort;

    TaskToDoTestSupport(TestRestTemplate restTemplate, int serverPort) {
        this.restTemplate = restTemplate;
        this.serverPort = serverPort;
    }


    ResponseEntity<Void> postTaskToDo(UserData parentUserData, ChildView childView, TaskView taskView, TaskToDoData taskToDoData) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .postForEntity(getUrl() + "?childId=" + childView.getId() + "&taskId=" + taskView.getId(), taskToDoData, Void.class);
    }

    ResponseEntity<TaskToDoView> getTaskToDo(UserData parentUserData, String location) {
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .getForEntity(location, TaskToDoView.class);
    }

    private String getUrl() {
        return "http://localhost:" + serverPort + "/api/tasksToDo";
    }
}