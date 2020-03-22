package pl.antonina.tasks.reward;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.antonina.tasks.cart.HistoryService;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildNotExistsException;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.security.LoggedUserService;

import java.security.Principal;
import java.util.List;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;
    private final LoggedUserService loggedUserService;
    private final ChildRepository childRepository;
    private final HistoryService historyService;

    public RewardService(RewardRepository rewardRepository,
                         LoggedUserService loggedUserService,
                         ChildRepository childRepository, HistoryService historyService) {
        this.rewardRepository = rewardRepository;
        this.loggedUserService = loggedUserService;
        this.childRepository = childRepository;
        this.historyService = historyService;
    }

    List<Reward> getRewardsByChildAndNotBought(Principal childPrincipal) {
        Child child = loggedUserService.getChild(childPrincipal);
        return rewardRepository.findByBoughtAndChild(false, child);
    }

    List<Reward> getRewardsByChildAndNotBought(long childId) {
        Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotExistsException("Child with id=" + childId + " doesn't exist."));
        return rewardRepository.findByBoughtAndChild(false, child);
    }

    void addReward(Principal childPrincipal, RewardData rewardData) {
        Child child = loggedUserService.getChild(childPrincipal);
        Reward reward = new Reward();
        reward.setChild(child);
        reward.setName(rewardData.getName());
        reward.setUrl(rewardData.getUrl());
        reward.setPoints(rewardData.getPoints());
        rewardRepository.save(reward);
    }

    void deleteReward(long rewardId) {
        rewardRepository.deleteById(rewardId);
    }

    @Transactional
    void setBought(long rewardId) {
        Reward reward = rewardRepository.findById(rewardId).orElseThrow(() -> new RewardNotExistsException("Reward with id=" + rewardId + " doesn't exist."));
        Integer childPoints = reward.getChild().getPoints();
        Integer newPoints = childPoints - reward.getPoints();

        if (newPoints < 0) {
            throw new IllegalArgumentException("Child has not enough points=" + childPoints + " to buy this reward=" + reward.getPoints() + ".");
        }

        reward.setBought(true);
        rewardRepository.save(reward);

        historyService.addHistory(reward);

        Child child = childRepository.findById(reward.getChild().getId()).orElseThrow(() -> new ChildNotExistsException("Child with id=" + reward.getChild().getId() + " doesn't exist."));
        child.setPoints(newPoints);
        childRepository.save(child);
    }
}