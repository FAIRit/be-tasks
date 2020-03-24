package pl.antonina.tasks.allegro.accessToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private final AccessTokenConfig accessTokenConfig = new AccessTokenConfig();

    @Override
    public String getAccessToken() {

        Call call = httpClient.newCall(accessTokenConfig.getRequest());

        try {
            Response response = call.execute();
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBody responseBody = response.body();
            AccessTokenResponse accessTokenResponse = objectMapper.readValue(responseBody.string(), AccessTokenResponse.class);
            return accessTokenResponse.getAccessToken();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}