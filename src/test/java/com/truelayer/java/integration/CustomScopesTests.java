package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.TestUtils.getClientCredentials;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.truelayer.java.Constants;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.TrueLayerClient;
import com.truelayer.java.auth.entities.GenerateOauthTokenRequest;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import com.truelayer.java.payments.entities.CreatePaymentResponse;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomScopesTests extends IntegrationTests {
    private static final List<String> CUSTOM_SCOPES =
            Collections.unmodifiableList(Arrays.asList(Constants.Scopes.PAYMENTS, "signupplus"));

    @BeforeEach
    @Override
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .withDefaultScopes(RequestScopes.builder().scopes(CUSTOM_SCOPES).build())
                .environment(testEnvironment)
                .withCredentialsCaching()
                .build();
    }

    @Test
    @DisplayName("It should create a payment using the global custom scopes")
    @SneakyThrows
    public void shouldCreateAPaymentWithGlobalCustomScopes() {
        stubGenerateTokenRequest();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .withIdempotencyKey()
                .status(201)
                .bodyFile("payments/201.create_payment.authorized.json")
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        verifyTokenUsed();
        assertNotError(createPaymentResponse);
    }

    private void stubGenerateTokenRequest() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
    }

    // TODO: move this into a test utils and add this assertion on default-scoped tests as well.
    private void verifyTokenUsed() {
        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath(
                                "$.client_id", equalTo(getClientCredentials().clientId())))
                        .withRequestBody(matchingJsonPath(
                                "$.client_secret",
                                equalTo(getClientCredentials().clientSecret())))
                        .withRequestBody(matchingJsonPath("$.scope", equalTo(String.join(" ", CUSTOM_SCOPES))))
                        .withRequestBody(matchingJsonPath(
                                "$.grant_type",
                                equalTo(GenerateOauthTokenRequest.GrantType.CLIENT_CREDENTIALS.getType()))));
    }
}
