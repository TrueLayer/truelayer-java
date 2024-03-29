package com.truelayer.java.auth;

import com.truelayer.java.ClientCredentials;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.auth.entities.GenerateOauthTokenRequest;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Value;

/**
 * {@inheritDoc}
 */
@Value
public class AuthenticationHandler implements IAuthenticationHandler {

    ClientCredentials clientCredentials;

    IAuthenticationApi authenticationApi;

    public static AuthenticationHandlerBuilder New() {
        return new AuthenticationHandlerBuilder();
    }

    @Override
    public CompletableFuture<ApiResponse<AccessToken>> getOauthToken(List<String> scopes) {
        GenerateOauthTokenRequest generateOauthTokenRequest = GenerateOauthTokenRequest.builder()
                .clientId(clientCredentials.clientId())
                .clientSecret(clientCredentials.clientSecret())
                .scopes(scopes)
                .build();

        return authenticationApi.generateOauthToken(generateOauthTokenRequest);
    }
}
