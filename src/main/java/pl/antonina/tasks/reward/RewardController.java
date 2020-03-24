package pl.antonina.tasks.reward;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/byChild")
    public List<Reward> getRewardsByChildAndNotBought(@ApiIgnore Principal childPrincipal) {
        return rewardService.getRewardsByChildAndNotBought(childPrincipal);
    }

    @GetMapping
    public List<Reward> getRewardsByChildAndNotBought(@RequestParam long childId) {
        return rewardService.getRewardsByChildAndNotBought(childId);
    }

    @PostMapping
    public void addReward(@ApiIgnore Principal childPrincipal, @Validated @RequestBody RewardData rewardData) {
        rewardService.addReward(childPrincipal, rewardData);
    }

    @PutMapping("{rewardId}/bought")
    public void setBought(@PathVariable long rewardId){
        rewardService.setBought(rewardId);
    }

    @DeleteMapping("{rewardId}")
    public void deleteReward(@PathVariable long rewardId){
        rewardService.deleteReward(rewardId);
    }
}