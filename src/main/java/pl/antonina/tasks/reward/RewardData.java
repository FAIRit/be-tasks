package pl.antonina.tasks.reward;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RewardData {
    @NotEmpty
    private String name;
    @NotEmpty
    private String url;
    @NotNull
    private Integer points;
}