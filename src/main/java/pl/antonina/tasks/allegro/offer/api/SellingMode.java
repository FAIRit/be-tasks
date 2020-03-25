package pl.antonina.tasks.allegro.offer.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellingMode {

    private Price price;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public BigDecimal getPriceAmount() {
        return price.getAmount();
    }
}