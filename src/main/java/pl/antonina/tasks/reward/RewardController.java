package pl.antonina.tasks.reward;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

import static pl.antonina.tasks.util.LocationUtils.getLocation;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/byChild")
    public List<RewardView> getRewardsByChildAndNotBought(@ApiIgnore Principal childPrincipal) {
        return rewardService.getRewardsByChildAndNotBought(childPrincipal);
    }

    @GetMapping
    public List<RewardView> getRewardsByChildAndNotBought(@ApiIgnore Principal parentPrincipal, @RequestParam long childId) {
        return rewardService.getRewardsByChildAndNotBought(parentPrincipal, childId);
    }

    @PostMapping
    public ResponseEntity<Void> addReward(@ApiIgnore Principal childPrincipal, @Validated @RequestBody RewardData rewardData) {
        long rewardId = rewardService.addReward(childPrincipal, rewardData);
        return ResponseEntity.created(getLocation(rewardId)).build();
    }

    @PutMapping("{rewardId}/bought")
    public void setBought(@ApiIgnore Principal parentPrincipal, @PathVariable long rewardId) {
        rewardService.setBought(parentPrincipal, rewardId);
    }

    @DeleteMapping("{rewardId}")
    public void deleteReward(@ApiIgnore Principal childPrincipal, @PathVariable long rewardId) {
        rewardService.deleteReward(childPrincipal, rewardId);
    }
}