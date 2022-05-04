package com.truelayer.java.contract;

import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.TestUtils.readJsonFile;

import au.com.dius.pact.consumer.dsl.FormPostBuilder;
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
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class PaymentsContractTests extends ContractTests {

    public static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    public static final String JWT_REGEX = "(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)";

    @SneakyThrows
    @Pact(consumer = "JavaSDK", provider = "PaymentsV3")
    RequestResponsePact createPayment(PactDslWithProvider builder) {
        return builder.uponReceiving("Create token call")
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
                .body(readJsonFile("auth/200.access_token.json"))
                .uponReceiving("Create payment call")
                .matchHeader(Constants.HeaderNames.IDEMPOTENCY_KEY, UUID_REGEX)
                .matchHeader(Constants.HeaderNames.TL_SIGNATURE, JWT_REGEX)
                .method("POST")
                .path("/payments")
                .body(Utils.getObjectMapper().writeValueAsString(getCreatePaymentRequest()), "application/json")
                .willRespondWith()
                .status(201)
                .body(readJsonFile("payments/201.create_payment.merchant_account.json"))
                .uponReceiving("Start authorization flow call")
                .matchHeader(Constants.HeaderNames.IDEMPOTENCY_KEY, UUID_REGEX)
                .matchHeader(Constants.HeaderNames.TL_SIGNATURE, JWT_REGEX)
                .method("POST")
                .matchPath("/payments/" + UUID_REGEX + "/authorization-flow")
                .body(Utils.getObjectMapper().writeValueAsString(getStartAuthFlowRequest()), "application/json")
                .willRespondWith()
                .status(200)
                .body(readJsonFile("payments/200.start_authorization_flow.redirect.json"))
                .toPact();
    }

    @SneakyThrows
    @Test
    @PactTestFor(pactMethod = "createPayment")
    public void shouldCreatePayment() {
        // 1. Create a payment with preselected provider
        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(getCreatePaymentRequest()).get();

        // 2. Start the auth flow
        ApiResponse<StartAuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), getStartAuthFlowRequest())
                .get();

        assertNotError(createPaymentResponse);
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
}
