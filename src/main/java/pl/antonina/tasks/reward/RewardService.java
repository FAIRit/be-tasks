package pl.antonina.tasks.reward;

import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

public interface RewardService {

    List<Reward> getRewardsByChildAndNotBought(Principal childPrincipal);

    List<Reward> getRewardsByChildAndNotBought(Principal parentPrincipal, long childId);

    long addReward(Principal childPrincipal, RewardData rewardData);

    void deleteReward(Principal childPrincipal, long rewardId);

    @Transactional
    void setBought(Principal parentPrincipal, long rewardId);
}