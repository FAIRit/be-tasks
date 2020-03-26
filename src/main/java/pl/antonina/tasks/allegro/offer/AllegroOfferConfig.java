package pl.antonina.tasks.allegro.offer;

import okhttp3.Request;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "allegro.offers")
class AllegroOfferConfig {
    private int sellerId;
    private int categoryId;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    Request getRequest(String accessToken) {
        return new Request.Builder()
                .url("https://api.allegro.pl/offers/listing?seller.id=" + sellerId + "&category.id=" + categoryId)
                .addHeader("Accept", "application/vnd.allegro.public.v1+json")
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();
    }
}