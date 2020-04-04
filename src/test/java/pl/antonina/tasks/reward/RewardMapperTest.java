package pl.antonina.tasks.reward;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RewardMapperTest {

    private RewardMapper rewardMapper = new RewardMapper();

    @Test
    void mapRewardView() {
        final String name = "Zabawka";
        final String url = "Http://something";
        final Integer points = 15;
        Reward reward = new Reward();
        reward.setName(name);
        reward.setPoints(points);
        reward.setUrl(url);

        RewardView rewardView = rewardMapper.mapRewardView(reward);

        assertThat(rewardView.getName()).isEqualTo(name);
        assertThat(rewardView.getUrl()).isEqualTo(url);
        assertThat(rewardView.getPoints()).isEqualTo(points);
    }
}