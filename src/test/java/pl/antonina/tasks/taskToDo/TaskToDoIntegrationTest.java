package pl.antonina.tasks.taskToDo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.child.ChildTestSupport;
import pl.antonina.tasks.child.ChildView;
import pl.antonina.tasks.parent.ParentTestSupport;
import pl.antonina.tasks.task.TaskTestSupport;
import pl.antonina.tasks.task.TaskView;
import pl.antonina.tasks.user.UserData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskToDoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int serverPort;

    private TaskToDoData taskToDoData;
    private TaskToDoData newTaskToDoData;
    private UserData parentUserData;
    private UserData childUserData;
    private TaskView taskView;
    private ChildView childView;
    private TaskToDoTestSupport taskToDoTestSupport;

    @BeforeEach
    void beforeEach() {
        taskToDoData = TaskToDoCreator.createTaskToDoData();
        newTaskToDoData = TaskToDoCreator.createNewTaskToDo();
        taskToDoTestSupport = new TaskToDoTestSupport(restTemplate, serverPort);

        ParentTestSupport parentTestSupport = new ParentTestSupport(restTemplate, serverPort);
        parentUserData = parentTestSupport.createParent();
        TaskTestSupport taskTestSupport = new TaskTestSupport(restTemplate, serverPort);
        taskView = taskTestSupport.createTask(parentUserData).getSecond();
        ChildTestSupport childTestSupport = new ChildTestSupport(restTemplate, serverPort);
        Pair<UserData, ChildView> pairChild = childTestSupport.createChild(parentUserData);
        childUserData = pairChild.getFirst();
        childView = pairChild.getSecond();
    }

    @Test
    void getTasksToDoByChild() {
        ResponseEntity<Void> responseFirstPostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responseFirstPostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String locationFirst = responseFirstPostEntity.getHeaders().getLocation().toString();
        ResponseEntity<TaskToDoView> responseFirstGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, locationFirst);
        assertThat(responseFirstGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskToDoView taskToDoViewFirst = responseFirstGetEntity.getBody();

        ResponseEntity<Void> responseSecondPostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, newTaskToDoData);
        assertThat(responseSecondPostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String locationSecond = responseSecondPostEntity.getHeaders().getLocation().toString();
        ResponseEntity<TaskToDoView> responseSecondGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, locationSecond);
        assertThat(responseSecondGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskToDoView taskToDoViewSecond = responseSecondGetEntity.getBody();

        ResponseEntity<List<TaskToDoView>> responseGetListEntity = taskToDoTestSupport.getTasksToDoByChild(childUserData);
        assertThat(responseGetListEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskToDoView> tasksToDoViewList = responseGetListEntity.getBody();

        assertThat(tasksToDoViewList).containsExactlyInAnyOrder(taskToDoViewFirst, taskToDoViewSecond);
    }

    @Test
    void getTasksToDoByParent() {
        ResponseEntity<Void> responseFirstPostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responseFirstPostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String locationFirst = responseFirstPostEntity.getHeaders().getLocation().toString();
        ResponseEntity<TaskToDoView> responseFirstGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, locationFirst);
        assertThat(responseFirstGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskToDoView taskToDoViewFirst = responseFirstGetEntity.getBody();

        ResponseEntity<Void> responseSecondPostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, newTaskToDoData);
        assertThat(responseSecondPostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String locationSecond = responseSecondPostEntity.getHeaders().getLocation().toString();
        ResponseEntity<TaskToDoView> responseSecondGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, locationSecond);
        assertThat(responseSecondGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskToDoView taskToDoViewSecond = responseSecondGetEntity.getBody();

        ResponseEntity<List<TaskToDoView>> responseGetListEntity = taskToDoTestSupport.getTasksToDoByParent(parentUserData, childView);
        assertThat(responseGetListEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskToDoView> tasksToDoViewList = responseGetListEntity.getBody();

        assertThat(tasksToDoViewList).containsExactlyInAnyOrder(taskToDoViewFirst, taskToDoViewSecond);
    }

    @Test
    void addTaskToDo() {
        ResponseEntity<Void> responsePostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        assertTaskToDo(taskToDoData, location);
    }

    @Test
    void updateTaskToDo() {
        ResponseEntity<Void> responsePostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<Void> responsePutEntity = taskToDoTestSupport.putTaskToDo(parentUserData, location, newTaskToDoData);
        assertThat(responsePutEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertTaskToDo(newTaskToDoData, location);
    }

    @Test
    void deleteTaskToDo() {
        ResponseEntity<Void> responsePostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<Void> responseDeleteEntity = taskToDoTestSupport.deleteTaskToDo(parentUserData, location);
        assertThat(responseDeleteEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<TaskToDoView> responseGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void setDoneTaskToDo() {
        ResponseEntity<Void> responsePostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<Void> responseSetDoneEntity = taskToDoTestSupport.setDoneTaskToDo(childUserData, location);
        assertThat(responseSetDoneEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertTaskToDo(taskToDoData, location);
    }

    @Test
    void setApprovedTaskToDo() {
        ResponseEntity<Void> responsePostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();
        ResponseEntity<Void> responseSetApproved = taskToDoTestSupport.setApprovedTaskToDo(parentUserData, location);
        assertThat(responseSetApproved.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertTaskToDo(taskToDoData, location);
    }

    private void assertTaskToDo(TaskToDoData taskToDoData, String location) {
        ResponseEntity<TaskToDoView> responseGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskToDoView actualTaskToDo = responseGetEntity.getBody();

        assertThat(actualTaskToDo.getExpectedDate()).isEqualTo(taskToDoData.getExpectedDate());
    }
}
