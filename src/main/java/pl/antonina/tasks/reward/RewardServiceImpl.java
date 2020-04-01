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
    public List<Reward> getRewardsByChildAndNotBought(Principal parentPrincipal, long childId) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        Child child = childRepository.findByIdAndParentId(childId, parentId)
                .orElseThrow(() -> new ChildNotExistsException("Child with id=" + childId + " doesn't exist."));
        return rewardRepository.findByBoughtAndChildAndChildParentId(false, child, parentId);
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
    public void deleteReward(Principal childPrincipal, long rewardId) {
        long childId = loggedUserService.getChild(childPrincipal).getId();
        Reward reward = rewardRepository.findByIdAndChildId(rewardId, childId)
                .orElseThrow(() -> new RewardNotExistsException("Reward with id=" + rewardId + " doesn't exist."));
        rewardRepository.delete(reward);
    }

    @Override
    @Transactional
    public void setBought(Principal parentPrincipal, long rewardId) {
        long parentId = loggedUserService.getParent(parentPrincipal).getId();
        Reward reward = rewardRepository.findByIdAndChildParentId(rewardId, parentId)
                .orElseThrow(() -> new RewardNotExistsException("Reward with id=" + rewardId + " doesn't exist."));
        Integer childPoints = reward.getChild().getPoints();
        Integer newPoints = childPoints - reward.getPoints();

        if (newPoints < 0) {
            throw new IllegalArgumentException("Child has not enough points=" + childPoints + " to buy this reward=" + reward.getPoints() + ".");
        }

        reward.setBought(true);
        rewardRepository.save(reward);

        historyService.addHistory(reward);

        Child child = childRepository.findByIdAndParentId(reward.getChild().getId(), parentId)
                .orElseThrow(() -> new ChildNotExistsException("Child with id=" + reward.getChild().getId() + " doesn't exist."));
        child.setPoints(newPoints);
        childRepository.save(child);
    }
}