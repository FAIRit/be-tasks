package pl.antonina.tasks.allegro.offer.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferResponse {

    private final Items items;

    public OfferResponse(@JsonProperty("items") Items items) {
        this.items = items;
    }

    public Items getItems() {
        return items;
    }
}