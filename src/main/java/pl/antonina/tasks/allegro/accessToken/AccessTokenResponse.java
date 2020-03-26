package pl.antonina.tasks.allegro.accessToken;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class AccessTokenResponse {

    private final String accessToken;

    public AccessTokenResponse(@JsonProperty("access_token") String accessToken) {
        this.accessToken = accessToken;
    }

    String getAccessToken() {
        return accessToken;
    }
}