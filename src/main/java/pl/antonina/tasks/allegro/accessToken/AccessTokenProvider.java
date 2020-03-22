package pl.antonina.tasks.allegro.accessToken;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;

@Service
public class AccessTokenProvider {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    public String getAccessToken() {
        RequestBody formBody = new FormBody.Builder().build();
        String clientId = "2d28293921ae472c9d82d99b2d9268eb";
        String clientSecret = "k0GW7MrzsCaIvN3o8NkPQx1GdsjBhEpFZG1dzYaj0NdFP8B27vHTAA1K5SRIXTys";
        String basicAuth = Credentials.basic(clientId, clientSecret);

        Request request = new Request.Builder()
                .url("https://allegro.pl/auth/oauth/token?grant_type=client_credentials")
                .addHeader("Authorization", basicAuth)
                .post(formBody)
                .build();

        Call call = httpClient.newCall(request);

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