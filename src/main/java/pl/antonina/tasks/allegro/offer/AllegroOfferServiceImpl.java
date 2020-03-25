package pl.antonina.tasks.allegro.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
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
    private final AllegroOfferConfig allegroOfferConfig = new AllegroOfferConfig();

    public AllegroOfferServiceImpl(AccessTokenService accessTokenService,
                                   OfferViewMapper offerViewMapper) {
        this.accessTokenService = accessTokenService;
        this.offerViewMapper = offerViewMapper;
    }

    @Override
    public List<OfferView> getOffers() {

        Call call = httpClient.newCall(allegroOfferConfig.getRequest(accessTokenService.getAccessToken()));

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