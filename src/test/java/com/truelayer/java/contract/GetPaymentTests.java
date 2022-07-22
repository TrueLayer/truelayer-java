package com.truelayer.java.contract;

import static com.truelayer.java.TestUtils.assertError;
import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.contract.Constant.*;
import static com.truelayer.java.contract.ContractsUtils.*;
import static com.truelayer.java.entities.beneficiary.Beneficiary.Type.MERCHANT_ACCOUNT;
import static com.truelayer.java.entities.providerselection.ProviderSelection.Type.USER_SELECTED;
import static com.truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.BANK_TRANSFER;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetPaymentTests extends ContractTests {
    @SneakyThrows
    @Override
    @Pact(consumer = CONSUMER_NAME, provider = PROVIDER_NAME)
    public RequestResponsePact buildPact(PactDslWithProvider builder) {
        NotFound(builder);
        return AuthorizationRequired(builder);
    }

    private RequestResponsePact NotFound(PactDslWithProvider builder) {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("content-type", "application/problem+json;charset=utf-8");
        return builder.given("Get Payment - Not Found")
                .uponReceiving("Get Payment - Not Found")
                .method("GET")
                .path(String.format("/payments/%s", ANOTHER_PAYMENT_ID))
                .willRespondWith()
                .status(404)
                .headers(responseHeaders)
                .body(new PactDslJsonBody()
                        .stringValue("type", "https://docs.truelayer.com/docs/error-types#not-found")
                        .stringValue("title", "Not Found")
                        .numberValue("status", 404)
                        .stringType("trace_id", "53967d20065852ee6b4a7a8aed932311")
                        .stringType("detail"))
                .toPact();
    }

    private RequestResponsePact AuthorizationRequired(PactDslWithProvider builder) {
        Map<String, Object> user_selected_params = new HashMap<>();
        user_selected_params.put("create_payment_request", SerializeContractBody(buildCreatePaymentRequest()));
        return builder.given("Get Payment - Authorization Required", user_selected_params)
                .uponReceiving("Get Payment - Authorization Required")
                .method("GET")
                .pathFromProviderState("/payments/${payment_id}", String.format("/payments/%s", A_PAYMENT_ID))
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .uuid("id", A_PAYMENT_ID)
                        .integerType("amount_in_minor", 1)
                        .stringMatcher("currency", CURRENCY_REGEX, CurrencyCode.GBP.toString())
                        .stringMatcher("created_at", ISO_8106_REGEX, "2022-01-18T10:46:20.489654Z")
                        .stringValue("status", Status.AUTHORIZATION_REQUIRED.getStatus())
                        .object("payment_method")
                        .stringValue("type", BANK_TRANSFER.getType())
                        .object("provider_selection")
                        .stringValue("type", USER_SELECTED.getType())
                        .closeObject()
                        .object("beneficiary")
                        .stringValue("type", MERCHANT_ACCOUNT.getType())
                        .stringType("merchant_account_id", "B7DAEA71-592E-486B-9C00-7132D1FD7AD1")
                        .closeObject()
                        .closeObject())
                .toPact();
    }

    @SneakyThrows
    @Override
    @DisplayName("It should get a payment")
    @PactTestFor(providerName = PROVIDER_NAME)
    @Test
    public void test() {
        assertNotError(tlClient.payments().getPayment(A_PAYMENT_ID).get());

        assertError(tlClient.payments().getPayment(ANOTHER_PAYMENT_ID).get());
    }
}
