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
        TaskToDoView taskToDoViewFirst = postAndGetTaskToDo(taskToDoData);
        TaskToDoView taskToDoViewSecond = postAndGetTaskToDo(newTaskToDoData);

        ResponseEntity<List<TaskToDoView>> responseGetListEntity = taskToDoTestSupport.getTasksToDoByChild(childUserData);
        assertThat(responseGetListEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskToDoView> tasksToDoViewList = responseGetListEntity.getBody();

        assertThat(tasksToDoViewList).containsExactlyInAnyOrder(taskToDoViewFirst, taskToDoViewSecond);
    }

    @Test
    void getTasksToDoByParent() {
        TaskToDoView taskToDoViewFirst = postAndGetTaskToDo(taskToDoData);
        TaskToDoView taskToDoViewSecond = postAndGetTaskToDo(newTaskToDoData);

        ResponseEntity<List<TaskToDoView>> responseGetListEntity = taskToDoTestSupport.getTasksToDoByParent(parentUserData, childView);
        assertThat(responseGetListEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<TaskToDoView> tasksToDoViewList = responseGetListEntity.getBody();

        assertThat(tasksToDoViewList).containsExactlyInAnyOrder(taskToDoViewFirst, taskToDoViewSecond);
    }

    @Test
    void addTaskToDo() {
        TaskToDoView taskToDoView = postAndGetTaskToDo(taskToDoData);
        assertTaskToDo(taskToDoData, taskToDoView);
    }

    @Test
    void updateTaskToDo() {
        String location = postAndGetLocation(taskToDoData);

        ResponseEntity<Void> responsePutEntity = taskToDoTestSupport.putTaskToDo(parentUserData, location, newTaskToDoData);
        assertThat(responsePutEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskToDoView taskToDoView = getTaskToDoView(location);

        assertTaskToDo(newTaskToDoData, taskToDoView);
    }

    @Test
    void deleteTaskToDo() {
        String location = postAndGetLocation(taskToDoData);

        ResponseEntity<Void> responseDeleteEntity = taskToDoTestSupport.deleteTaskToDo(parentUserData, location);
        assertThat(responseDeleteEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<TaskToDoView> responseGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void setDoneTaskToDo() {
        String location = postAndGetLocation(taskToDoData);

        ResponseEntity<Void> responseSetDoneEntity = taskToDoTestSupport.setDoneTaskToDo(childUserData, location);
        assertThat(responseSetDoneEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskToDoView taskToDoView = getTaskToDoView(location);

        assertThat(taskToDoView.isDone()).isTrue();
        assertThat(taskToDoView.getFinishDate()).isNotNull();
    }

    @Test
    void setApprovedTaskToDo() {
        String location = postAndGetLocation(taskToDoData);

        ResponseEntity<Void> responseSetApproved = taskToDoTestSupport.setApprovedTaskToDo(parentUserData, location);
        assertThat(responseSetApproved.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void assertTaskToDo(TaskToDoData taskToDoData, TaskToDoView taskToDoView) {
        assertThat(taskToDoView.getExpectedDate()).isEqualTo(taskToDoData.getExpectedDate());
        assertThat(taskToDoView.getTaskView()).isEqualTo(taskView);
        assertThat(taskToDoView.getStartDate()).isNotNull();
        assertThat(taskToDoView.getFinishDate()).isNull();
        assertThat(taskToDoView.isDone()).isFalse();
    }

    private String postAndGetLocation(TaskToDoData taskToDoData) {
        ResponseEntity<Void> responsePostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return responsePostEntity.getHeaders().getLocation().toString();
    }

    private TaskToDoView getTaskToDoView(String location) {
        ResponseEntity<TaskToDoView> responseGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseGetEntity.getBody();
    }

    private TaskToDoView postAndGetTaskToDo(TaskToDoData taskToDoData) {
        String location = postAndGetLocation(taskToDoData);
        return getTaskToDoView(location);
    }
}