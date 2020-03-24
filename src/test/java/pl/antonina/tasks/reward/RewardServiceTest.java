package pl.antonina.tasks.reward;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.antonina.tasks.cart.HistoryService;
import pl.antonina.tasks.cart.HistoryServiceImpl;
import pl.antonina.tasks.child.Child;
import pl.antonina.tasks.child.ChildRepository;
import pl.antonina.tasks.security.LoggedUserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private RewardRepository rewardRepository;
    @Mock
    private LoggedUserService loggedUserService;
    @Mock
    private ChildRepository childRepository;
    @Mock
    private HistoryService historyService;

    private RewardService rewardService;

    @Captor
    private ArgumentCaptor<Reward> rewardArgumentCaptor;

    @Captor
    ArgumentCaptor<Child> childArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        rewardService = new RewardServiceImpl(rewardRepository, loggedUserService, childRepository, historyService);
    }

    @Test
    void getRewardsByChildAndNotBoughtByChildPrincipal() {
        Reward reward1 = new Reward();
        Reward reward2 = new Reward();
        final List<Reward> rewardList = List.of(reward1, reward2);

        Principal childPrincipal = mock(Principal.class);
        Child child = new Child();
        when(loggedUserService.getChild(childPrincipal)).thenReturn(child);
        when(rewardRepository.findByBoughtAndChild(false, child)).thenReturn(rewardList);

        List<Reward> rewardListResult = rewardService.getRewardsByChildAndNotBought(childPrincipal);

        assertThat(rewardListResult).isEqualTo(rewardList);
    }

    @Test
    void testGetRewardsByChildAndNotBoughtByChildId() {
        Reward reward1 = new Reward();
        Reward reward2 = new Reward();
        final List<Reward> rewardList = List.of(reward1, reward2);
        long childId = 123;

        Child child = new Child();
        when(childRepository.findById(childId)).thenReturn(Optional.of(child));
        when(rewardRepository.findByBoughtAndChild(false, child)).thenReturn(rewardList);
        List<Reward> rewardListResult = rewardService.getRewardsByChildAndNotBought(childId);

        assertThat(rewardListResult).isEqualTo(rewardList);
    }

    @Test
    void addReward() {
        final String name = "Zabawka";
        final String url = "Http/blablabl";
        final Integer points = 50;

        RewardData rewardData = new RewardData();
        rewardData.setName(name);
        rewardData.setUrl(url);
        rewardData.setPoints(points);

        Principal childPrincipal = mock(Principal.class);
        final Child child = new Child();
        when(loggedUserService.getChild(childPrincipal)).thenReturn(child);

        rewardService.addReward(childPrincipal, rewardData);

        verify(rewardRepository).save(rewardArgumentCaptor.capture());
        Reward rewardCaptured = rewardArgumentCaptor.getValue();

        assertThat(rewardCaptured.getChild()).isEqualTo(child);
        assertThat(rewardCaptured.getName()).isEqualTo(name);
        assertThat(rewardCaptured.getUrl()).isEqualTo(url);
        assertThat(rewardCaptured.getPoints()).isEqualTo(points);
    }

    @Test
    void deleteReward() {
        long rewardId = 123;
        rewardService.deleteReward(rewardId);
        verify(rewardRepository).deleteById(rewardId);
    }

    @Test
    void setBought() {
        Child child = new Child();
        Integer childPoints = 50;
        long childId = 987;
        child.setId(childId);
        child.setPoints(childPoints);

        Reward reward = new Reward();
        long rewardId = 123;
        Integer rewardPoints = 20;
        reward.setId(rewardId);
        reward.setChild(child);
        reward.setPoints(rewardPoints);

        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(childRepository.findById(childId)).thenReturn(Optional.of(child));

        rewardService.setBought(rewardId);

        verify(rewardRepository).save(rewardArgumentCaptor.capture());
        Reward rewardCaptured = rewardArgumentCaptor.getValue();

        verify(childRepository).save(childArgumentCaptor.capture());
        Child childCaptured = childArgumentCaptor.getValue();

        assertThat(rewardCaptured.isBought()).isTrue();
        assertThat(childCaptured.getPoints()).isEqualTo(childPoints - rewardPoints);

    }
}