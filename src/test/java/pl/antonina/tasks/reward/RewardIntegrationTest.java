package pl.antonina.tasks.reward;

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
import pl.antonina.tasks.task.TaskData;
import pl.antonina.tasks.task.TaskTestSupport;
import pl.antonina.tasks.task.TaskView;
import pl.antonina.tasks.taskToDo.TaskToDoTestSupport;
import pl.antonina.tasks.user.UserData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RewardIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int serverPort;

    private RewardTestSupport rewardTestSupport;
    private RewardData rewardData;
    private RewardData newRewardData;

    private UserData parentUserData;
    private UserData childUserData;
    private ChildView childView;


    @BeforeEach
    void beforeEach() {
        rewardTestSupport = new RewardTestSupport(restTemplate, serverPort);
        rewardData = RewardCreator.createRewardData();
        newRewardData = RewardCreator.createNewRewardData();

        ParentTestSupport parentTestSupport = new ParentTestSupport(restTemplate, serverPort);
        parentUserData = parentTestSupport.createParent();
        ChildTestSupport childTestSupport = new ChildTestSupport(restTemplate, serverPort);
        Pair<UserData, ChildView> childPair = childTestSupport.createChild(parentUserData);
        childUserData = childPair.getFirst();
        childView = childPair.getSecond();
    }

    @Test
    void addReward() {
        postRewardData(rewardData);
        RewardView rewardView = getRewardsView().get(0);

        assertReward(rewardData, rewardView);
    }

    @Test
    void getRewardsByParent() {
        postRewardData(rewardData);
        postRewardData(newRewardData);
        List<RewardView> rewardViewList = getRewardsView();
        RewardView rewardView = rewardViewList.get(0);
        RewardView newRewardView = rewardViewList.get(1);

        ResponseEntity<List<RewardView>> responseGetEntity = rewardTestSupport.getRewardsByParent(parentUserData, childView);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<RewardView> rewardViewListByParent = responseGetEntity.getBody();

        assertThat(rewardViewListByParent).containsExactlyInAnyOrder(rewardView, newRewardView);
        assertThat(rewardViewListByParent).containsAll(rewardViewList);
    }

    @Test
    void setBoughtIfChildHaveEnoughPoints() {
        TaskTestSupport taskTestSupport = new TaskTestSupport(restTemplate, serverPort);
        Pair<TaskData, TaskView> taskPair = taskTestSupport.createTask(parentUserData);
        TaskView taskView = taskPair.getSecond();

        TaskToDoTestSupport taskToDoTestSupport = new TaskToDoTestSupport(restTemplate, serverPort);
        taskToDoTestSupport.createAndApprovedTaskToDo(parentUserData, childView, taskView);

        postRewardData(rewardData);
        RewardView rewardView = getRewardsView().get(0);

        ResponseEntity<Void> responseEntity = rewardTestSupport.setBought(parentUserData, rewardView);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldntSetBoughtIfChildHaveNotEnoughPoints() {
        postRewardData(rewardData);
        RewardView rewardView = getRewardsView().get(0);

        ResponseEntity<Void> responseEntity = rewardTestSupport.setBought(parentUserData, rewardView);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteReward() {
        postRewardData(rewardData);
        RewardView rewardView = getRewardsView().get(0);

        ResponseEntity<Void> responseEntity = rewardTestSupport.deleteReward(childUserData, rewardView);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(getRewardsView()).isEmpty();
    }

    private void assertReward(RewardData rewardData, RewardView rewardView) {
        assertThat(rewardView.getName()).isEqualTo(rewardData.getName());
        assertThat(rewardView.getUrl()).isEqualTo(rewardData.getUrl());
        assertThat(rewardView.getPoints()).isEqualTo(rewardData.getPoints());
    }

    private void postRewardData(RewardData rewardData) {
        ResponseEntity<Void> responsePostEntity = rewardTestSupport.postReward(childUserData, rewardData);
        assertThat(responsePostEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private List<RewardView> getRewardsView() {
        ResponseEntity<List<RewardView>> responseGetEntity = rewardTestSupport.getRewardsByChild(childUserData);
        assertThat(responseGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        return responseGetEntity.getBody();
    }
}