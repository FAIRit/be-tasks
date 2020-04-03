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
        ResponseEntity<Void> responsePostTaskEntity = taskTestSupport.postTask(parentUserData, taskData);
        assertThat(responsePostTaskEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String taskLocation = responsePostTaskEntity.getHeaders().getLocation().toString();
        ResponseEntity<TaskView> responseGetTaskEntity = taskTestSupport.getTask(parentUserData, taskLocation);
        assertThat(responseGetTaskEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskView taskView = responseGetTaskEntity.getBody();

        ResponseEntity<Void> responsePostNewTaskEntity = taskTestSupport.postTask(parentUserData, newTaskData);
        assertThat(responsePostNewTaskEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String newTaskLocation = responsePostNewTaskEntity.getHeaders().getLocation().toString();
        ResponseEntity<TaskView> responseGetNewTaskEntity = taskTestSupport.getTask(parentUserData, newTaskLocation);
        assertThat(responseGetNewTaskEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskView newTaskView = responseGetNewTaskEntity.getBody();

        ResponseEntity<List<TaskView>> responseGetEntity = taskTestSupport.getTasks(parentUserData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<TaskView> taskViews = responseGetEntity.getBody();

        assertThat(taskViews).containsExactlyInAnyOrder(taskView, newTaskView);
    }

    @Test
    void addTask() {
        ResponseEntity<Void> responsePostEntity = taskTestSupport.postTask(parentUserData, taskData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        assertTask(taskData, location);
    }

    @Test
    void updateTask() {
        ResponseEntity<Void> responsePostEntity = taskTestSupport.postTask(parentUserData, taskData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<Void> responsePutEntity = taskTestSupport.putTask(location, parentUserData, newTaskData);
        assertThat(responsePutEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertTask(newTaskData, location);
    }

    @Test
    void deleteTask() {
        ResponseEntity<Void> responsePostEntity = taskTestSupport.postTask(parentUserData, taskData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<Void> responseDeleteEntity = taskTestSupport.deleteTask(location, parentUserData);
        assertThat(responseDeleteEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<TaskView> responseGetEntity = taskTestSupport.getTask(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void assertTask(TaskData taskData, String location) {
        ResponseEntity<TaskView> responseGetEntity = taskTestSupport.getTask(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskView actualTask = responseGetEntity.getBody();
        assertThat(actualTask.getDescription()).isEqualTo(taskData.getDescription());
        assertThat(actualTask.getName()).isEqualTo(taskData.getName());
        assertThat(actualTask.getPoints()).isEqualTo(taskData.getPoints());
    }
}