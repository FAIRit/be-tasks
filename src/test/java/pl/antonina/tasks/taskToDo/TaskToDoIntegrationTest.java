package pl.antonina.tasks.taskToDo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.child.ChildTestSupport;
import pl.antonina.tasks.child.ChildView;
import pl.antonina.tasks.parent.ParentTestSupport;
import pl.antonina.tasks.task.TaskTestSupport;
import pl.antonina.tasks.task.TaskView;
import pl.antonina.tasks.user.UserData;

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
        childView = childTestSupport.createChild(parentUserData).getSecond();
    }

    @Test
    void addTaskToDo() {
        ResponseEntity<Void> responsePostEntity = taskToDoTestSupport.postTaskToDo(parentUserData, childView, taskView, taskToDoData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = responsePostEntity.getHeaders().getLocation().toString();

        assertTaskToDo(taskToDoData, location);
    }


    private void assertTaskToDo(TaskToDoData taskToDoData, String location) {
        ResponseEntity<TaskToDoView> responseGetEntity = taskToDoTestSupport.getTaskToDo(parentUserData, location);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        TaskToDoView actualTaskToDo = responseGetEntity.getBody();
        assertThat(actualTaskToDo.getExpectedDate()).isEqualTo(taskToDoData.getExpectedDate());
    }
}
