package pl.antonina.tasks.reward;

import lombok.Data;

@Data
public class RewardView {
    private long id;
    private String name;
    private String url;
    private Integer points;
}