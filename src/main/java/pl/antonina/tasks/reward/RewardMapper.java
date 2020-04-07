package pl.antonina.tasks.reward;

import org.springframework.stereotype.Component;

@Component
class RewardMapper {

    RewardView mapRewardView(Reward reward) {
        RewardView rewardView = new RewardView();
        rewardView.setId(reward.getId());
        rewardView.setName(reward.getName());
        rewardView.setUrl(reward.getUrl());
        rewardView.setPoints(reward.getPoints());
        return rewardView;
    }
}