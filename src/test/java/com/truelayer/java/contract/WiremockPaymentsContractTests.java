package com.truelayer.java.contract;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.TestUtils;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.ProblemDetails;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import com.truelayer.java.payments.entities.paymentmethod.provider.ProviderFilter;
import com.truelayer.java.payments.entities.paymentmethod.provider.ProviderSelection;
import java.net.URI;
import java.util.Collections;
import java.util.UUID;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Deprecated
@Disabled
public class WiremockPaymentsContractTests extends WiremockContractTests {

    @Test
    @DisplayName("It should complete a payment flow with preselected provider")
    @SneakyThrows
    public void itShouldCreateAPayment() {
        TestUtils.RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .withResponseBodyFile("auth/200.access_token.json")
                .build();
        TestUtils.RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .withResponseBodyFile("payments/201.create_payment.merchant_account.json")
                .build();
        TestUtils.RequestStub.New()
                .method("post")
                .path(urlPathMatching("/payments/.*/authorization-flow"))
                .withAuthorization()
                .status(200)
                .withResponseBodyFile("payments/200.start_authorization_flow.redirect.json")
                .build();

        // 1. Create a payment with preselected provider
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(RandomUtils.nextInt(50, 500))
                .currency(CurrencyCode.GBP)
                .paymentMethod(PaymentMethod.bankTransfer()
                        .providerSelection(ProviderSelection.userSelected()
                                .filter(ProviderFilter.builder()
                                        .countries(Collections.singletonList(CountryCode.GB))
                                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                                        .providerIds(Collections.singletonList("mock-payments-gb-redirect"))
                                        .build())
                                .build())
                        .beneficiary(Beneficiary.merchantAccount()
                                .merchantAccountId(UUID.randomUUID().toString())
                                .build())
                        .build())
                .user(User.builder()
                        .name("Contract test user")
                        .email("contract-test@truelayer.com")
                        .build())
                .build();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        // todo: what kind of assertions do we want here ?
        assertNotError(createPaymentResponse);

        // 2. Start the authorization flow and complete the payment
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create("http://localhost:8080/callback"))
                        .build())
                .withProviderSelection()
                .build();

        ApiResponse<StartAuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        // todo: what kind of assertions do we want here ?
        assertNotError(createPaymentResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should return a request invalid error")
    public void shouldThrowARequestInvalidError() {
        String jsonResponseFile = "payments/400.request_invalid.json";
        TestUtils.RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .withResponseBodyFile("auth/200.access_token.json")
                .build();
        TestUtils.RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(400)
                .withResponseBodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> paymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertTrue(paymentResponse.isError());
        ProblemDetails expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }
}
