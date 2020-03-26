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
import java.util.List;

@Service
public class AllegroOfferServiceImpl implements AllegroOfferService {

    private final AccessTokenService accessTokenService;
    private final OfferViewMapper offerViewMapper;
    private final AllegroOfferConfig allegroOfferConfig;
    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public AllegroOfferServiceImpl(AccessTokenService accessTokenService,
                                   OfferViewMapper offerViewMapper,
                                   AllegroOfferConfig allegroOfferConfig) {
        this.accessTokenService = accessTokenService;
        this.offerViewMapper = offerViewMapper;
        this.allegroOfferConfig = allegroOfferConfig;
    }

    @Override
    public List<OfferView> getOffers() throws IOException {

        String accessToken = accessTokenService.getAccessToken();

        Call call = httpClient.newCall(allegroOfferConfig.getRequest(accessToken));

        Response response = call.execute();
        ResponseBody responseBody = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        OfferResponse offerResponse = objectMapper.readValue(responseBody.string(), OfferResponse.class);
        Items items = offerResponse.getItems();
        return offerViewMapper.mapOfferViews(items);
    }
}