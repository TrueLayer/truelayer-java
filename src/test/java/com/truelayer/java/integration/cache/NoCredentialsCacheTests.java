package com.truelayer.java.integration.cache;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.HeaderNames.AUTHORIZATION;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.TrueLayerClient;
import com.truelayer.java.integration.IntegrationTests;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import java.net.URI;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Simple test for a client built without credentials caching capabilities
 */
public class NoCredentialsCacheTests extends IntegrationTests {

    // overrides the default implementation by not setting the cache for credentials
    @BeforeEach
    @Override
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(testEnvironment)
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("It should use a a fresh token when creating multiple payments")
    public void itShouldUseACachedToken() {
        String accessTokenJsonFile = "auth/200.access_token.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile(accessTokenJsonFile)
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .bodyFile("payments/201.create_payment.authorization_required.json")
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        tlClient.payments().createPayment(paymentRequest).get();
        tlClient.payments().createPayment(paymentRequest).get();

        verify(2, postRequestedFor(urlPathEqualTo("/connect/token")));
        verify(2, postRequestedFor(urlPathEqualTo("/payments")).withHeader(AUTHORIZATION, matching("Bearer .*")));
    }
}
