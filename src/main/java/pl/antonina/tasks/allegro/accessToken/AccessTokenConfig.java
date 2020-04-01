package pl.antonina.tasks.allegro.accessToken;

import lombok.Data;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "allegro.auth")
class AccessTokenConfig {
    private String clientId;
    private String clientSecret;

    Request getRequest() {
        final RequestBody formBody = new FormBody.Builder().build();
        return new Request.Builder()
                .url("https://allegro.pl/auth/oauth/token?grant_type=client_credentials")
                .addHeader("Authorization", Credentials.basic(clientId, clientSecret))
                .post(formBody)
                .build();
    }
}