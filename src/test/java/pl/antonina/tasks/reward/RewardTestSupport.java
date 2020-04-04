package pl.antonina.tasks.reward;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import pl.antonina.tasks.child.ChildView;
import pl.antonina.tasks.user.UserData;

import java.util.List;

public class RewardTestSupport {

    private final TestRestTemplate restTemplate;
    private final int serverPort;

    public RewardTestSupport(TestRestTemplate restTemplate, int serverPort) {
        this.restTemplate = restTemplate;
        this.serverPort = serverPort;
    }

    ResponseEntity<List<RewardView>> getRewardsByChild(UserData childUserData) {
        return restTemplate
                .withBasicAuth(childUserData.getEmail(), childUserData.getPassword())
                .exchange(getRewardsUrl() + "/byChild", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
                });
    }

    ResponseEntity<List<RewardView>> getRewardsByParent(UserData parentUserData, ChildView childView) {
        String url = getRewardsUrl() + "?childId=" + childView.getId();
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
                });
    }

    ResponseEntity<Void> postReward(UserData childUserData, RewardData rewardData) {
        return restTemplate
                .withBasicAuth(childUserData.getEmail(), childUserData.getPassword())
                .postForEntity(getRewardsUrl(), rewardData, Void.class);
    }

    ResponseEntity<Void> setBought(UserData parentUserData, RewardView rewardView) {
        String url = getRewardsUrl() + "/" + rewardView.getId() + "/bought";
        return restTemplate
                .withBasicAuth(parentUserData.getEmail(), parentUserData.getPassword())
                .exchange(url, HttpMethod.PUT, HttpEntity.EMPTY, Void.class);
    }

    ResponseEntity<Void> deleteReward(UserData childUserData, RewardView rewardView) {
        String url = getRewardsUrl() + "/" + rewardView.getId();
        return restTemplate
                .withBasicAuth(childUserData.getEmail(), childUserData.getPassword())
                .exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
    }

    public void createAndSetBoughtReward(UserData childUserData, UserData parentUserData) {
        RewardData rewardData = RewardCreator.createRewardData();
        postReward(childUserData, rewardData);
        ResponseEntity<List<RewardView>> responseGetEntity = getRewardsByChild(childUserData);
        RewardView rewardView = responseGetEntity.getBody().get(0);
        setBought(parentUserData, rewardView);
    }

    private String getRewardsUrl() {
        return "Http://localhost:" + serverPort + "/api/rewards";
    }
}