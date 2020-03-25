package pl.antonina.tasks.allegro.accessToken;

import java.io.IOException;

public interface AccessTokenService {

    String getAccessToken() throws IOException;
}