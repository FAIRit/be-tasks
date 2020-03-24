package pl.antonina.tasks.allegro.offer;

import okhttp3.Request;
import org.springframework.context.annotation.Configuration;
import pl.antonina.tasks.allegro.accessToken.AccessTokenService;

@Configuration
public class AllegroOfferConfig {

    Request getRequest(AccessTokenService accessTokenService) {
        return new Request.Builder()
                .url("https://api.allegro.pl/offers/listing?seller.id=1680&category.id=11818")
                .addHeader("Accept", "application/vnd.allegro.public.v1+json")
                .addHeader("Authorization", "Bearer " + accessTokenService.getAccessToken())
                .get()
                .build();
    }
}