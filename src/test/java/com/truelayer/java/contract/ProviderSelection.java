package com.truelayer.java.contract;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.AuthorizationFlowResponse;
import com.truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.contract.Constant.*;
import static com.truelayer.java.contract.ContractsUtils.*;

public class ProviderSelection extends ContractTests {

    @SneakyThrows
    @Override
    @Pact(consumer = CONSUMER_NAME, provider = PROVIDER_NAME)
    public RequestResponsePact buildPact(PactDslWithProvider builder) {
        return ProviderSelection(builder);
    }

    @SneakyThrows
    @Override
    @DisplayName("It should submit the selected provider")
    @PactTestFor(providerName = PROVIDER_NAME)
    @Test
    public void test() {
        ApiResponse<AuthorizationFlowResponse> submitProviderResponse = tlClient.payments()
                .submitProviderSelection(A_PAYMENT_ID, buildProviderSelectionRequest())
                .get();
        assertNotError(submitProviderResponse);
    }

    private RequestResponsePact ProviderSelection(PactDslWithProvider builder) {
        Map<String, Object> provider_selection_params = new HashMap<>();
        provider_selection_params.put("return_uri", RETURN_URI);
        provider_selection_params.put("provider_id", PROVIDER_ID_GB);
        provider_selection_params.put("create_payment_request", SerializeContractBody(buildCreatePaymentRequest(com.truelayer.java.entities.providerselection.ProviderSelection.Type.USER_SELECTED)));
        return builder
                .given("Submit Provider", provider_selection_params)
                .uponReceiving("Submit Provider")
                .method("POST")
                .pathFromProviderState(
                        "/payments/${payment_id}/authorization-flow/actions/provider-selection",
                        String.format("/payments/%s/authorization-flow/actions/provider-selection", A_PAYMENT_ID))
                .body(SerializeContractBody(buildProviderSelectionRequest()), "application/json")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringValue("status", Status.AUTHORIZING.getStatus())
                        .object("authorization_flow")
                        .object("actions")
                        .object("next")
                        .stringValue("type", AuthorizationFlowAction.Type.REDIRECT.getType())
                        .stringType("uri", RETURN_URI)
                        .closeObject()
                        .closeObject()
                        .closeObject())
                .toPact();
    }
}
