package pl.antonina.tasks.allegro.offer.api;

import java.util.List;

public class Items {

    private List<Offer> promoted;
    private List<Offer> regular;

    public List<Offer> getPromoted() {
        return promoted;
    }

    public void setPromoted(List<Offer> promoted) {
        this.promoted = promoted;
    }

    public List<Offer> getRegular() {
        return regular;
    }

    public void setRegular(List<Offer> regular) {
        this.regular = regular;
    }
}