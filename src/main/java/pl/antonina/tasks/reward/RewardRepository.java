package pl.antonina.tasks.reward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.parent.Parent;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByBoughtAndChildAndChildParentId(boolean bought, Child child, long parentId);

    List<Reward> findByBoughtAndChild(boolean bought, Child child);

    Optional<Reward> findByIdAndChildId(long rewardId, long childId);

    Optional<Reward> findByIdAndChildParentId(long rewardId, long parentId);
}