package com.truelayer.java.integration.cache;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static com.truelayer.java.TestUtils.deserializeJsonFileTo;
import static java.lang.Thread.sleep;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.integration.IntegrationTests;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

@DisplayName("Access token cache integration tests")
public class DefaultAccessTokenCacheTests extends IntegrationTests {

    @SneakyThrows
    @Test
    @DisplayName("It should use a cached token when creating multiple payments")
    public void itShouldUseACachedToken() {
        String accessTokenJsonFile = "auth/200.access_token.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .withResponseBodyFile(accessTokenJsonFile)
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .withResponseBodyFile("payments/201.create_payment.merchant_account.json")
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
    }

    @SneakyThrows
    @Test
    @DisplayName("It should refresh the access token in case of 401")
    public void itShouldRefreshTheAccessTokenIfUnauthorized() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .withResponseBodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(401)
                .withResponseBodyFile("payments/401.invalid_token.json")
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        tlClient.payments().createPayment(paymentRequest).get();
        tlClient.payments().createPayment(paymentRequest).get();

        verify(2, postRequestedFor(urlPathEqualTo("/connect/token")));
        verify(2, postRequestedFor(urlPathEqualTo("/payments")));
    }

    @SneakyThrows
    @Test
    @DisplayName("It should refresh the access token if expired")
    public void itShouldRefreshTheAccessTokenIfExpired() throws InterruptedException {
        String accessTokenImmediateExpirationJsonFile = "auth/200.access_token.immediate_expiration.json";
        String accessTokenJsonFile = "auth/200.access_token.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .withResponseBodyFile(accessTokenImmediateExpirationJsonFile)
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .withResponseBodyFile("payments/201.create_payment.merchant_account.json")
                .build();
        AccessToken expectedImmediateExpirationToken =
                deserializeJsonFileTo(accessTokenImmediateExpirationJsonFile, AccessToken.class);
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        tlClient.payments().createPayment(paymentRequest).get();
        sleep(expectedImmediateExpirationToken.getExpiresIn() * 1000L);
        // below will supersede previous similar request
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .withResponseBodyFile(accessTokenJsonFile)
                .build();
        tlClient.payments().createPayment(paymentRequest).get();

        verify(2, postRequestedFor(urlPathEqualTo("/connect/token")));
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/payments"))
                        .withHeader(
                                AUTHORIZATION, equalTo("Bearer " + expectedImmediateExpirationToken.getAccessToken())));
        AccessToken expectedToken = deserializeJsonFileTo(accessTokenJsonFile, AccessToken.class);
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/payments"))
                        .withHeader(AUTHORIZATION, equalTo("Bearer " + expectedToken.getAccessToken())));
    }
}
