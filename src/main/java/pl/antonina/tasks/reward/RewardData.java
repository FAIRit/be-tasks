package pl.antonina.tasks.reward;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class RewardData {
    @NotEmpty
    private String name;
    @NotEmpty
    private String url;
    @NotEmpty
    private Integer points;
}