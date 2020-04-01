package pl.antonina.tasks.allegro.offer;

import lombok.Data;
import okhttp3.Request;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "allegro.offers")
class AllegroOfferConfig {
    private int sellerId;
    private int categoryId;

    Request getRequest(String accessToken) {
        return new Request.Builder()
                .url("https://api.allegro.pl/offers/listing?seller.id=" + sellerId + "&category.id=" + categoryId)
                .addHeader("Accept", "application/vnd.allegro.public.v1+json")
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();
    }
}