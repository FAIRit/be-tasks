package pl.antonina.tasks.task;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int serverPort;

    private TaskTestSupport taskTestSupport;
    private TaskData taskData;
    private TaskData newTaskData;
    private UserData parentUserData;

    @BeforeEach
    void beforeEach() {
        taskData = TaskCreator.createTaskData();
        newTaskData = TaskCreator.createNewTaskData();
        taskTestSupport = new TaskTestSupport(restTemplate, serverPort);
        ParentTestSupport parentTestSupport = new ParentTestSupport(restTemplate, serverPort);
        parentUserData = parentTestSupport.createParent();
    }

    @Test
    void getTasks() {
        TaskView firstTaskView = postAndGetTaskView(taskData);
        TaskView secondTaskView = postAndGetTaskView(newTaskData);

        ResponseEntity<List<TaskView>> responseGetEntity = taskTestSupport.getTasks(parentUserData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<TaskView> taskViews = responseGetEntity.getBody();

        assertThat(taskViews).containsExactlyInAnyOrder(firstTaskView, secondTaskView);
    }

    @Test
    void addTask() {
        TaskView taskView = postAndGetTaskView(taskData);

        assertTaskData(taskView, taskData);
    }

    @Test
    void updateTask() {
        String location = postAndGetLocation(newTaskData);

        ResponseEntity<Void> responsePutEntity = taskTestSupport.putTask(location, parentUserData, newTaskData);
        assertThat(responsePutEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskView taskView = getTaskView(location);

        assertTaskData(taskView, newTaskData);
    }

    @Test
    void deleteTask() {
        String location = postAndGetLocation(taskData);
        ResponseEntity<Void> responseDeleteEntity = taskTestSupport.deleteTask(location, parentUserData);
        assertThat(responseDeleteEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<TaskView> responseGetEntity = taskTestSupport.getTask(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void assertTaskData(TaskView actualTaskView, TaskData taskData) {
        assertThat(actualTaskView.getName()).isEqualTo(taskData.getName());
        assertThat(actualTaskView.getDescription()).isEqualTo(taskData.getDescription());
        assertThat(actualTaskView.getPoints()).isEqualTo(taskData.getPoints());
        assertThat(actualTaskView.getId()).isNotNull();
    }

    private String postAndGetLocation(TaskData taskData) {
        ResponseEntity<Void> responsePostTaskEntity = taskTestSupport.postTask(parentUserData, taskData);
        assertThat(responsePostTaskEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return responsePostTaskEntity.getHeaders().getLocation().toString();
    }

    private TaskView getTaskView(String location) {
        ResponseEntity<TaskView> responseGetTaskEntity = taskTestSupport.getTask(parentUserData, location);
        assertThat(responseGetTaskEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseGetTaskEntity.getBody();
    }

    private TaskView postAndGetTaskView(TaskData taskData) {
        String location = postAndGetLocation(taskData);
        return getTaskView(location);
    }
}