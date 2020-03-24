package pl.antonina.tasks.allegro.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;
import pl.antonina.tasks.allegro.accessToken.AccessTokenService;
import pl.antonina.tasks.allegro.offer.api.Items;
import pl.antonina.tasks.allegro.offer.api.OfferResponse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Service
public class AllegroOfferServiceImpl implements AllegroOfferService {

    private final AccessTokenService accessTokenService;
    private final OfferViewMapper offerViewMapper;
    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public AllegroOfferServiceImpl(AccessTokenService accessTokenService,
                                   OfferViewMapper offerViewMapper) {
        this.accessTokenService = accessTokenService;
        this.offerViewMapper = offerViewMapper;
    }

    @Override
    public List<OfferView> getOffers() {
        Request request = new Request.Builder()
                .url("https://api.allegro.pl/offers/listing?seller.id=1680&category.id=11818")
                .addHeader("Accept", "application/vnd.allegro.public.v1+json")
                .addHeader("Authorization", "Bearer " + accessTokenService.getAccessToken())
                .get()
                .build();

        Call call = httpClient.newCall(request);

        try {
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            OfferResponse offerResponse = objectMapper.readValue(responseBody.string(), OfferResponse.class);
            Items items = offerResponse.getItems();
            return offerViewMapper.mapOfferViews(items);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}