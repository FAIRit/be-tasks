package pl.antonina.tasks.allegro.accessToken;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessTokenConfig {

    private String clientId = "2d28293921ae472c9d82d99b2d9268eb";
    private String clientSecret = "k0GW7MrzsCaIvN3o8NkPQx1GdsjBhEpFZG1dzYaj0NdFP8B27vHTAA1K5SRIXTys";
    private String basicAuth = Credentials.basic(clientId, clientSecret);
    private RequestBody formBody = new FormBody.Builder().build();

    public Request getRequest() {
        return new Request.Builder()
                .url("https://allegro.pl/auth/oauth/token?grant_type=client_credentials")
                .addHeader("Authorization", basicAuth)
                .post(formBody)
                .build();
    }
}