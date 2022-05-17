package com.truelayer.java.contract;

import au.com.dius.pact.consumer.dsl.FormPostBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslResponse;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.truelayer.java.ClientCredentials;
import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
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
import java.util.UUID;
import lombok.SneakyThrows;
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

    @SneakyThrows
    @Pact(consumer = "JavaSDK", provider = "PaymentsV3")
    RequestResponsePact createPayment(PactDslWithProvider builder) {
        PactDslResponse pactContract;
        pactContract = this.testAuthToken(builder);
        pactContract = this.testCreatePayment(pactContract);
        pactContract = this.testStartAuthFlow(pactContract);
        return pactContract.toPact();
    }

    private PactDslResponse testAuthToken(PactDslWithProvider builder) {
        return builder.given("Auth Token state")
                .uponReceiving("Create token call")
                .method("POST")
                .path("/connect/token")
                .body(
                        new String(new FormPostBuilder()
                                .stringMatcher(
                                        "client_id",
                                        TestUtils.getClientCredentials().clientId())
                                .stringValue(
                                        "client_secret",
                                        TestUtils.getClientCredentials().clientSecret())
                                .stringValue("grant_type", ClientCredentials.GRANT_TYPE)
                                .stringValue("scopes", Constants.Scopes.PAYMENTS)
                                .buildBody()),
                        "application/x-www-form-urlencoded")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringMatcher("access_token", JWT_TOKEN_REGEX, A_JWT_TOKEN)
                        .numberType("expires_in"));
    }

    @SneakyThrows
    private PactDslResponse testCreatePayment(PactDslResponse pactContract) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put(Constants.HeaderNames.IDEMPOTENCY_KEY, UUID.randomUUID().toString());
        return pactContract.given("Create Payment state")
                .uponReceiving("Create payment call")
                .method("POST")
                .path("/payments")
                .headers(headers)
                .headerFromProviderState(Constants.HeaderNames.AUTHORIZATION, "access_token", "Bearer " + TestUtils.buildAccessTokenPlain().getAccessToken())
                .body(Utils.getObjectMapper().writeValueAsString(getCreatePaymentRequest()), "application/json")
                .willRespondWith()
                .status(201)
                .body(CreatePaymentMerchantAccountResponseBody());
    }

    @SneakyThrows
    private PactDslResponse testStartAuthFlow(PactDslResponse pactContract) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put(Constants.HeaderNames.IDEMPOTENCY_KEY, UUID.randomUUID().toString());
        return pactContract.given("Authorize Payment state")
                .uponReceiving("Start authorization flow call")
                .headers(headers)
                .headerFromProviderState(Constants.HeaderNames.AUTHORIZATION, "access_token", "Bearer " + TestUtils.buildAccessTokenPlain())
                .method("POST")
                .pathFromProviderState(
                        "/payments/${payment_id}/authorization-flow",
                        "/payments/48c890dc-8c03-428c-9a8b-2f383fd0ba38/authorization-flow")
                .body(Utils.getObjectMapper().writeValueAsString(getStartAuthFlowRequest()), "application/json")
                .willRespondWith()
                .status(200)
                .body(startAuthorizationFlowRedirectResponseBody());
    }

    @SneakyThrows
    @Test
    @PactTestFor(pactMethod = "createPayment")
    public void shouldCreatePayment() {
        // 1. Create a payment with preselected provider
        CreatePaymentRequest createPaymentRequest = getCreatePaymentRequest();
        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(createPaymentRequest).get();

        // 2. Start the auth flow
        tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), getStartAuthFlowRequest())
                .get();
    }

    private CreatePaymentRequest getCreatePaymentRequest() {
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
                                .merchantAccountId("a-merchant-id")
                                .build())
                        .build())
                .user(User.builder()
                        .name("Contract test user")
                        .email("contract-test@truelayer.com")
                        .build())
                .build();
    }

    private StartAuthorizationFlowRequest getStartAuthFlowRequest() {
        return StartAuthorizationFlowRequest.builder()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create("http://localhost:8080/callback"))
                        .build())
                .withProviderSelection()
                .build();
    }

    private PactDslJsonBody CreatePaymentMerchantAccountResponseBody() {
        return new PactDslJsonBody()
                .stringType("id", "48c890dc-8c03-428c-9a8b-2f383fd0ba38")
                .stringMatcher("resource_token", JWT_TOKEN_REGEX, A_JWT_TOKEN)
                .object("user")
                .stringMatcher("id", UUID_REGEX);
    }

    private PactDslJsonBody startAuthorizationFlowRedirectResponseBody() {
        return new PactDslJsonBody()
                .stringMatcher("status", PAYMENT_STATUS_REGEX, "authorizing")
                .object("authorization_flow")
                .object("actions")
                .object("next")
                .stringType("type", "redirect")
                .matchUrl("uri", "https://a-redirect-uri.com")
                .object("metadata")
                .stringType("type", "provider")
                .stringType("provider_id", "ob-bank-name")
                .stringType("display_name", "Bank Name")
                .matchUrl("icon_uri", "https://truelayer-provider-assets.s3.amazonaws.com/global/icon/generic.svg")
                .matchUrl("logo_uri", "https://truelayer-provider-assets.s3.amazonaws.com/global/logos/generic.svg")
                .stringType("bg_color", "#000000")
                .stringType("country_code", "GB");
    }
}
