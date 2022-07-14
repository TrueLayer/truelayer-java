package com.truelayer.java.contract;

import static com.truelayer.java.contract.Constant.*;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.truelayer.java.Utils;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.ProblemDetails;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import java.util.Collections;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreatePaymentInvalidRequest extends ContractTests {

    @SneakyThrows
    @Pact(consumer = CONSUMER_NAME, provider = PROVIDER_NAME)
    @Override
    public RequestResponsePact buildPact(PactDslWithProvider builder) {
        return builder.given("a state")
                .uponReceiving("Create payment call")
                .method("POST")
                .path("/payments")
                .body(Utils.getObjectMapper().writeValueAsString(buildCreatePaymentRequest()), "application/json")
                .willRespondWith()
                .status(400)
                .body(
                        Utils.getObjectMapper()
                                .writeValueAsString(ProblemDetails.builder()
                                        .status(400)
                                        .title("Invalid parameters")
                                        .traceId(UUID.randomUUID().toString())
                                        .build()),
                        "application/json")
                .toPact();
    }

    @SneakyThrows
    @Override
    @Test
    @PactTestFor(providerName = PROVIDER_NAME)
    @DisplayName("It should fail because user is missing")
    public void test() {
        CreatePaymentRequest createPaymentRequest = buildCreatePaymentRequest();
        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(createPaymentRequest).get();
        Assertions.assertTrue(createPaymentResponse.isError());
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
                                .merchantAccountId(MERCHANT_ACCOUNT_ID)
                                .build())
                        .build())
                .build();
    }
}
