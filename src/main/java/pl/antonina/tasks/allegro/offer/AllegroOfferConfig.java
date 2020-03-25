package pl.antonina.tasks.allegro.offer;

import okhttp3.Request;
import org.springframework.context.annotation.Configuration;

@Configuration
class AllegroOfferConfig {

    private int sellerId = 1680;
    private int categoryId = 11818;

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
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