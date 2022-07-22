package com.truelayer.java.contract;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.truelayer.java.Utils;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import com.truelayer.java.payments.entities.CreatePaymentResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.contract.Constant.*;
import static com.truelayer.java.contract.ContractsUtils.buildCreatePaymentRequest;

public class CreatePaymentSuccessTests extends ContractTests {

    @SneakyThrows
    @Override
    @Pact(consumer = CONSUMER_NAME, provider = PROVIDER_NAME)
    public RequestResponsePact buildPact(PactDslWithProvider builder) {
        Map<String, Object> createPaymentParams = new HashMap<>();
        createPaymentParams.put("merchant_account_id", MERCHANT_ACCOUNT_ID);
        return builder.given("Create Payment", createPaymentParams)
                .uponReceiving("Create Payment")
                .method("POST")
                .path("/payments")
                .body(Utils.getObjectMapper().writeValueAsString(buildCreatePaymentRequest()), "application/json")
                .willRespondWith()
                .status(201)
                .body(new PactDslJsonBody()
                        .uuid("id", A_PAYMENT_ID)
                        .stringMatcher("resource_token", JWT_TOKEN_REGEX, A_JWT_TOKEN)
                        .object("user")
                        .uuid("id"))
                .toPact();
    }

    @SneakyThrows
    @Override
    @DisplayName("It should create a payment")
    @PactTestFor(providerName = PROVIDER_NAME)
    @Test
    public void test() {
        CreatePaymentRequest createPaymentRequest = buildCreatePaymentRequest();
        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(createPaymentRequest).get();
        assertNotError(createPaymentResponse);
    }

}
