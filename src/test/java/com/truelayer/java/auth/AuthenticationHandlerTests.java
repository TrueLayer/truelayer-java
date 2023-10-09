package com.truelayer.java.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.truelayer.java.ClientCredentials;
import com.truelayer.java.TestUtils;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.auth.entities.GenerateOauthTokenRequest;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthenticationHandlerTests {

    @SneakyThrows
    @Test
    @DisplayName("It should get an access token")
    public void shouldYieldAnAccessToken() {
        String scope = "a-scope";
        ClientCredentials clientCredentials = TestUtils.getClientCredentials();
        IAuthenticationApi authenticationApi = mock(IAuthenticationApi.class);
        ApiResponse<AccessToken> expectedToken = TestUtils.buildAccessToken();
        GenerateOauthTokenRequest request = GenerateOauthTokenRequest.builder()
                .clientId(clientCredentials.clientId())
                .clientSecret(clientCredentials.clientSecret())
                .scope(scope)
                .build();
        when(authenticationApi.generateOauthToken(request))
                .thenReturn(CompletableFuture.completedFuture(expectedToken));
        AuthenticationHandler sut = new AuthenticationHandler(clientCredentials, authenticationApi);

        ApiResponse<AccessToken> token =
                sut.getOauthToken(Collections.singletonList(scope)).get();

        assertEquals(expectedToken, token);
    }
}
