package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static com.truelayer.java.TestUtils.deserializeJsonFileTo;
import static java.lang.Thread.sleep;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

/**
 * Test class meant to test OkHttp client interceptors and authenticator behavior
 * in integration mode. Deliberately kept simple due to the existing unit tests
 * on single collaborators.
 */
public class AccessTokenManagementTests extends IntegrationTests {

    @SneakyThrows
    @Test
    @DisplayName("It should use a cached token")
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
                .bodyFile("payments/201.create_payment.merchant_account.json")
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
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(401)
                .bodyFile("payments/401.invalid_token.json")
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
        long tokenExpirationMs = 1500;
        String accessTokenImmediateExpirationJsonFile = "auth/200.access_token.immediate_expiration.json";
        String accessTokenJsonFile = "auth/200.access_token.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile(accessTokenImmediateExpirationJsonFile)
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .bodyFile("payments/201.create_payment.merchant_account.json")
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        tlClient.payments().createPayment(paymentRequest).get();
        sleep(tokenExpirationMs);
        // below will supersede previous similar request
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile(accessTokenJsonFile)
                .build();
        tlClient.payments().createPayment(paymentRequest).get();

        verify(2, postRequestedFor(urlPathEqualTo("/connect/token")));
        AccessToken expectedImmediateExpirationToken =
                deserializeJsonFileTo(accessTokenImmediateExpirationJsonFile, AccessToken.class);
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
