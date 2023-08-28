package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.TestUtils.getClientCredentials;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerClient;
import com.truelayer.java.auth.entities.GenerateOauthTokenRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

@WireMockTest
@Tag("integration")
public abstract class IntegrationTests {
    protected TrueLayerClient tlClient;

    @BeforeEach
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(testEnvironment)
                .withCredentialsCaching()
                .build();
    }

    /**
     * Internal utility to check that the /connect/token endpoint was called with the expected details.
     * @param expectedScopes the scopes that we expect to be declared by the library
     */
    protected void verifyGeneratedToken(List<String> expectedScopes) {
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath(
                                "$.client_id", equalTo(getClientCredentials().clientId())))
                        .withRequestBody(matchingJsonPath(
                                "$.client_secret",
                                equalTo(getClientCredentials().clientSecret())))
                        .withRequestBody(matchingJsonPath("$.scope", equalTo(String.join(" ", expectedScopes))))
                        .withRequestBody(matchingJsonPath(
                                "$.grant_type",
                                equalTo(GenerateOauthTokenRequest.GrantType.CLIENT_CREDENTIALS.getType()))));
    }
}
