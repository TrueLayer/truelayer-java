package com.truelayer.java.auth;

import com.truelayer.java.ClientCredentials;
import com.truelayer.java.TrueLayerException;
import org.apache.commons.lang3.ObjectUtils;
import retrofit2.Retrofit;

public class AuthenticationHandlerBuilder {

    private Retrofit httpClient;

    private ClientCredentials clientCredentials;

    AuthenticationHandlerBuilder() {}

    public AuthenticationHandlerBuilder clientCredentials(ClientCredentials clientCredentials) {
        this.clientCredentials = clientCredentials;
        return this;
    }

    public AuthenticationHandlerBuilder httpClient(Retrofit httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public AuthenticationHandler build() {
        if (ObjectUtils.isEmpty(clientCredentials)) {
            throw new TrueLayerException("client credentials must be set");
        }
        return new AuthenticationHandler(clientCredentials, httpClient.create(IAuthenticationApi.class));
    }
}
