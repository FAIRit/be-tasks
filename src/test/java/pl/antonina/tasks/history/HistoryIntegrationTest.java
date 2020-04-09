package pl.antonina.tasks.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.child.ChildTestSupport;
import pl.antonina.tasks.child.ChildView;
import pl.antonina.tasks.parent.ParentTestSupport;
import pl.antonina.tasks.reward.RewardTestSupport;
import pl.antonina.tasks.task.TaskData;
import pl.antonina.tasks.task.TaskTestSupport;
import pl.antonina.tasks.task.TaskView;
import pl.antonina.tasks.taskToDo.TaskToDoTestSupport;
import pl.antonina.tasks.user.UserData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HistoryIntegrationTest {

    @Autowired
    private
    TestRestTemplate restTemplate;

    @LocalServerPort
    private
    int serverPort;

    private UserData parentUserData;
    private UserData childUserData;
    private ChildView childView;

    @BeforeEach
    void beforeEach() {
        ParentTestSupport parentTestSupport = new ParentTestSupport(restTemplate, serverPort);
        parentUserData = parentTestSupport.createParent();
        ChildTestSupport childTestSupport = new ChildTestSupport(restTemplate, serverPort);
        Pair<UserData, ChildView> childPair = childTestSupport.createChild(parentUserData);
        childUserData = childPair.getFirst();
        childView = childPair.getSecond();
    }

    @Test
    void getHistory() {
        createAndApproveTaskToDo();
        createAndSetBoughtReward();

        ResponseEntity<List<History>> responseGetEntity = restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(getHistoryUrl() + "/" + childView.getId(), HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
                });
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseGetEntity.getBody().size()).isEqualTo(2);
    }

    private void createAndApproveTaskToDo() {
        TaskTestSupport taskTestSupport = new TaskTestSupport(restTemplate, serverPort);
        Pair<TaskData, TaskView> taskPair = taskTestSupport.createTask(parentUserData);
        TaskView taskView = taskPair.getSecond();

        TaskToDoTestSupport taskToDoTestSupport = new TaskToDoTestSupport(restTemplate, serverPort);
        taskToDoTestSupport.createAndApprovedTaskToDo(parentUserData, childView, taskView);
    }

    private void createAndSetBoughtReward() {
        RewardTestSupport rewardTestSupport = new RewardTestSupport(restTemplate, serverPort);
        rewardTestSupport.createAndSetBoughtReward(childUserData, parentUserData);

    }

    private String getHistoryUrl() {
        return "http://localhost:" + serverPort + "/api/history";
    }
}