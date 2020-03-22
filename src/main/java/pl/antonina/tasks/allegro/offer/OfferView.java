package pl.antonina.tasks.allegro.offer;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OfferView {
    private String name;
    private String url;
    private BigDecimal price;
}
