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
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;
    private final LoggedUserService loggedUserService;
    private final ChildRepository childRepository;
    private final HistoryService historyService;

    public RewardServiceImpl(RewardRepository rewardRepository,
                             LoggedUserService loggedUserService,
                             ChildRepository childRepository,
                             HistoryService historyService) {
        this.rewardRepository = rewardRepository;
        this.loggedUserService = loggedUserService;
        this.childRepository = childRepository;
        this.historyService = historyService;
    }

    @Override
    public List<Reward> getRewardsByChildAndNotBought(Principal childPrincipal) {
        Child child = loggedUserService.getChild(childPrincipal);
        return rewardRepository.findByBoughtAndChild(false, child);
    }

    @Override
    public List<Reward> getRewardsByChildAndNotBought(long childId) {
        Child child = childRepository.findById(childId).orElseThrow(() -> new ChildNotExistsException("Child with id=" + childId + " doesn't exist."));
        return rewardRepository.findByBoughtAndChild(false, child);
    }

    @Override
    public void addReward(Principal childPrincipal, RewardData rewardData) {
        Child child = loggedUserService.getChild(childPrincipal);
        Reward reward = new Reward();
        reward.setChild(child);
        reward.setName(rewardData.getName());
        reward.setUrl(rewardData.getUrl());
        reward.setPoints(rewardData.getPoints());
        rewardRepository.save(reward);
    }

    @Override
    public void deleteReward(long rewardId) {
        rewardRepository.deleteById(rewardId);
    }

    @Override
    @Transactional
    public void setBought(long rewardId) {
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