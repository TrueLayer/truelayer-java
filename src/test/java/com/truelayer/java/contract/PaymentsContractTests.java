package com.truelayer.java.contract;

import static com.truelayer.java.TestUtils.assertNotError;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.truelayer.java.Utils;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import com.truelayer.java.payments.entities.paymentmethod.provider.ProviderFilter;
import com.truelayer.java.payments.entities.paymentmethod.provider.ProviderSelection;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PaymentsContractTests extends ContractTests {

    // Matchers
    public static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    public static final String JWT_TOKEN_REGEX = "[a-zA-Z0-9_-]*.[a-zA-Z0-9_-]*.[a-zA-Z0-9_-]*";
    public static final String PAYMENT_STATUS_REGEX =
            "^(authorization_required|authorizing|authorized|executed|settled|failed)$";

    // Samples
    public static final String A_JWT_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final String A_PAYMENT_ID = "48c890dc-8c03-428c-9a8b-2f383fd0ba38";
    public static final String merchantAccountId = "B7DAEA71-592E-486B-9C00-7132D1FD7AD1";
    public static final String returnUri = "http://localhost:8080/callback";

    @SneakyThrows
    @Pact(consumer = "JavaSDK", provider = "PaymentsV3")
    RequestResponsePact createAndAuthorizePayment(PactDslWithProvider builder) {
        Map<String, Object> createPaymentParams = new HashMap<>();
        createPaymentParams.put("merchant_account_id", merchantAccountId);
        Map<String, Object> authorizePaymentParams = new HashMap<>();
        authorizePaymentParams.put("return_uri", returnUri);

        return builder.given("Create Payment state", createPaymentParams)
                .uponReceiving("Create payment call")
                .method("POST")
                .path("/payments")
                .body(Utils.getObjectMapper().writeValueAsString(buildCreatePaymentRequest()), "application/json")
                .willRespondWith()
                .status(201)
                .body(new PactDslJsonBody()
                        .stringType("id", A_PAYMENT_ID)
                        .stringMatcher("resource_token", JWT_TOKEN_REGEX, A_JWT_TOKEN)
                        .object("user")
                        .stringMatcher("id", UUID_REGEX))
                .given("Authorize Payment state", authorizePaymentParams)
                .uponReceiving("Start authorization flow call")
                .method("POST")
                .pathFromProviderState(
                        "/payments/${payment_id}/authorization-flow",
                        String.format("/payments/%s/authorization-flow", A_PAYMENT_ID))
                .body(Utils.getObjectMapper().writeValueAsString(buildStartAuthFlowRequest()), "application/json")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringMatcher("status", PAYMENT_STATUS_REGEX, "authorizing")
                        .object("authorization_flow")
                        .object("actions")
                        .object("next")
                        .stringType("type", "provider_selection")
                        .array("providers")
                        .object()
                        .stringType("provider_id", "ob-bank-name")
                        .stringType("display_name", "Bank Name")
                        .matchUrl(
                                "icon_uri",
                                "https://truelayer-provider-assets.s3.amazonaws.com/global/icon/generic.svg")
                        .matchUrl(
                                "logo_uri",
                                "https://truelayer-provider-assets.s3.amazonaws.com/global/logos/generic.svg")
                        .stringType("bg_color", "#000000")
                        .stringType("country_code", "GB")
                        .closeArray()
                        .closeObject()
                        .closeObject()
                        .closeObject())
                .toPact();
    }

    @SneakyThrows
    @Test
    @DisplayName("It should create and authorize a payment")
    @PactTestFor(pactMethod = "createAndAuthorizePayment")
    public void shouldCreateAndAuthorizePayment() {
        // 1. Create a payment with preselected provider
        CreatePaymentRequest createPaymentRequest = buildCreatePaymentRequest();
        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(createPaymentRequest).get();
        assertNotError(createPaymentResponse);

        // 2. Start the auth flow
        ApiResponse<PaymentAuthorizationFlowResponse> authorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), buildStartAuthFlowRequest())
                .get();
        assertNotError(authorizationFlowResponse);
    }

    private CreatePaymentRequest buildCreatePaymentRequest() {
        return CreatePaymentRequest.builder()
                .amountInMinor(50)
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
                                .merchantAccountId(merchantAccountId)
                                .build())
                        .build())
                .user(User.builder()
                        .name("Contract test user")
                        .email("contract-test@truelayer.com")
                        .build())
                .build();
    }

    private StartAuthorizationFlowRequest buildStartAuthFlowRequest() {
        return StartAuthorizationFlowRequest.builder()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create(returnUri))
                        .build())
                .withProviderSelection()
                .build();
    }
}
