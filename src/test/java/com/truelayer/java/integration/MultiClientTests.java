package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.Constants.Scopes.SIGNUP_PLUS;
import static com.truelayer.java.TestUtils.*;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.truelayer.java.ClientCredentials;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerClient;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MultiClientTests extends IntegrationTests {

    private TrueLayerClient tlClient2;

    private String clientId1;
    private String clientSecret1;

    private String clientId2;
    private String clientSecret2;

    // overrides the default implementation to create 2 independent clients
    @BeforeEach
    @Override
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));
        clientId1 = UUID.randomUUID().toString();
        clientSecret1 = UUID.randomUUID().toString();
        clientId2 = UUID.randomUUID().toString();
        clientSecret2 = UUID.randomUUID().toString();

        tlClient = TrueLayerClient.New()
                .clientCredentials(ClientCredentials.builder()
                        .clientId(clientId1)
                        .clientSecret(clientSecret1)
                        .build())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(testEnvironment)
                .build();

        tlClient2 = TrueLayerClient.New()
                .clientCredentials(ClientCredentials.builder()
                        .clientId(clientId2)
                        .clientSecret(clientSecret2)
                        .build())
                .signingOptions(getSigningOptions())
                .environment(testEnvironment)
                .withCredentialsCaching()
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("It should use independent clients")
    public void itShouldUseIndependentClients() {
        String accessTokenJsonFile = "auth/200.access_token.json";
        TestUtils.RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile(accessTokenJsonFile)
                .build();

        tlClient.auth().getOauthToken(List.of(PAYMENTS)).get();
        tlClient2.auth().getOauthToken(List.of(SIGNUP_PLUS)).get();

        verify(2, postRequestedFor(urlPathEqualTo("/connect/token")));
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath("$.client_id", equalTo(clientId1))));
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath("$.client_secret", equalTo(clientSecret1))));
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath("$.client_id", equalTo(clientId2))));
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath("$.client_secret", equalTo(clientSecret2))));
    }
}
