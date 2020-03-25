package pl.antonina.tasks.allegro.offer.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer {

    private Long id;
    private String name;
    private List<Image> images;
    private SellingMode sellingMode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public SellingMode getSellingMode() {
        return sellingMode;
    }

    public void setSellingMode(SellingMode sellingMode) {
        this.sellingMode = sellingMode;
    }

    public BigDecimal getAmount(){
        return sellingMode.getPriceAmount();
    }

    public String getUrl(){
        return images.get(0).getUrl();
    }
}