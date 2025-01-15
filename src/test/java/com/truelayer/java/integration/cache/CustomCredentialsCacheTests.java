package com.truelayer.java.integration.cache;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static com.truelayer.java.TestUtils.deserializeJsonFileTo;
import static com.truelayer.java.TestUtils.getSigningOptions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.truelayer.java.ClientCredentials;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.TrueLayerClient;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.auth.cache.ICredentialsCache;
import com.truelayer.java.integration.IntegrationTests;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomCredentialsCacheTests extends IntegrationTests {

    private MyCustomCache customCredentialsCache;

    private TrueLayerClient tlClient2;
    private String clientId2;

    // overrides the default implementation by setting a custom cache for credentials
    @BeforeEach
    @Override
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));
        customCredentialsCache = new MyCustomCache();
        tlClient = TrueLayerClient.New()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(testEnvironment)
                .withCredentialsCaching(customCredentialsCache)
                .build();

        clientId2 = UUID.randomUUID().toString();
        tlClient2 = TrueLayerClient.New()
                .clientCredentials(ClientCredentials.builder()
                        .clientId(clientId2)
                        .clientSecret(UUID.randomUUID().toString())
                        .build())
                .signingOptions(getSigningOptions())
                .environment(testEnvironment)
                .withCredentialsCaching(customCredentialsCache)
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("It should use a cached token when creating multiple payments")
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

        verify(1, postRequestedFor(urlPathEqualTo("/connect/token")));
        AccessToken expectedToken = deserializeJsonFileTo(accessTokenJsonFile, AccessToken.class);
        verify(
                2,
                postRequestedFor(urlPathEqualTo("/payments"))
                        .withHeader(AUTHORIZATION, equalTo("Bearer " + expectedToken.getAccessToken())));

        assertEquals(1, customCredentialsCache.cache.mappingCount());
        assertEquals(
                expectedToken,
                customCredentialsCache.cache.entrySet().stream()
                        .findFirst()
                        .get()
                        .getValue());
    }

    @SneakyThrows
    @Test
    @DisplayName("Should keep credentials segregated when the same cache is used across different clients")
    public void itShouldUse2IndependentClients() {
        String clientId1 = TestUtils.getClientCredentials().clientId();
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
        tlClient2.payments().createPayment(paymentRequest).get();

        verify(2, postRequestedFor(urlPathEqualTo("/connect/token")));
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath("$.client_id", equalTo(clientId1))));
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath("$.client_id", equalTo(clientId2))));

        AccessToken expectedToken = deserializeJsonFileTo(accessTokenJsonFile, AccessToken.class);
        verify(
                2,
                postRequestedFor(urlPathEqualTo("/payments"))
                        .withHeader(AUTHORIZATION, equalTo("Bearer " + expectedToken.getAccessToken())));

        assertEquals(2, customCredentialsCache.cache.mappingCount());
        assertTrue(customCredentialsCache.cache.entrySet().stream()
                .anyMatch(k -> k.getKey().startsWith(MessageFormat.format("tl-auth-token:{0}", clientId1))));
        assertTrue(customCredentialsCache.cache.entrySet().stream()
                .anyMatch(k -> k.getKey().startsWith(MessageFormat.format("tl-auth-token:{0}", clientId2))));
    }

    public static class MyCustomCache implements ICredentialsCache {
        ConcurrentHashMap<String, AccessToken> cache;

        public MyCustomCache() {
            cache = new ConcurrentHashMap<>();
        }

        @Override
        public Optional<AccessToken> getToken(String key) {
            return Optional.ofNullable(cache.get(key));
        }

        @Override
        public void storeToken(String key, AccessToken token) {
            cache.put(key, token);
        }

        @Override
        public void clearToken(String key) {
            cache.remove(key);
        }
    }
}
