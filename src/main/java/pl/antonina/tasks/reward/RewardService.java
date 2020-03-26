package pl.antonina.tasks.reward;

import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

public interface RewardService {

    List<Reward> getRewardsByChildAndNotBought(Principal childPrincipal);

    List<Reward> getRewardsByChildAndNotBought(long childId);

    void addReward(Principal childPrincipal, RewardData rewardData);

    void deleteReward(long rewardId);

    @Transactional
    void setBought(long rewardId);
}