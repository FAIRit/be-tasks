package pl.antonina.tasks.reward;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.parent.Parent;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByBoughtAndChild(boolean bought, Child child);
}