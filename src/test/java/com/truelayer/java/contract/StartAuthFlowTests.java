package com.truelayer.java.contract;

import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.contract.Constant.*;
import static com.truelayer.java.contract.ContractsUtils.*;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.AuthorizationFlowResponse;
import com.truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StartAuthFlowTests extends ContractTests {

    @SneakyThrows
    @Override
    @Pact(consumer = CONSUMER_NAME, provider = PROVIDER_NAME)
    public RequestResponsePact buildPact(PactDslWithProvider builder) {
        Preselected(builder);
        return UserSelected(builder);
    }

    private RequestResponsePact UserSelected(PactDslWithProvider builder) {
        Map<String, Object> user_selected_params = new HashMap<>();
        user_selected_params.put("return_uri", RETURN_URI);
        user_selected_params.put(
                "create_payment_request",
                SerializeContractBody(buildCreatePaymentRequest(ProviderSelection.Type.USER_SELECTED)));
        return builder.given("Start Auth Flow - provider_selection", user_selected_params)
                .uponReceiving("Start Auth Flow - provider_selection")
                .method("POST")
                .pathFromProviderState(
                        "/payments/${payment_id}/authorization-flow",
                        String.format("/payments/%s/authorization-flow", A_PAYMENT_ID))
                .body(SerializeContractBody(buildStartAuthFlowRequest()), "application/json")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringValue("status", Status.AUTHORIZING.getStatus())
                        .object("authorization_flow")
                        .object("actions")
                        .object("next")
                        .stringValue("type", AuthorizationFlowAction.Type.PROVIDER_SELECTION.getType())
                        .array("providers")
                        .object()
                        .stringValue("id", PROVIDER_ID_GB)
                        .stringType("display_name", "Bank Name")
                        .matchUrl("icon_uri", "https://truelayer-provider-assets.s3.amazonaws.com/")
                        .matchUrl("logo_uri", "https://truelayer-provider-assets.s3.amazonaws.com/")
                        .stringMatcher("bg_color", RGB_REGEX, "#000000")
                        .stringMatcher("country_code", COUNTRY_CODE_REGEX, "GB")
                        .closeArray()
                        .closeObject()
                        .closeObject()
                        .closeObject())
                .toPact();
    }

    private void Preselected(PactDslWithProvider builder) {
        Map<String, Object> preselected_params = new HashMap<>();
        preselected_params.put("return_uri", RETURN_URI);
        preselected_params.put(
                "create_payment_request",
                SerializeContractBody(buildCreatePaymentRequest(ProviderSelection.Type.PRESELECTED)));
        builder.given("Start Auth Flow - redirect", preselected_params)
                .uponReceiving("Start Auth Flow - redirect")
                .method("POST")
                .pathFromProviderState(
                        "/payments/${payment_id}/authorization-flow",
                        String.format("/payments/%s/authorization-flow", A_PAYMENT_ID))
                .body(SerializeContractBody(buildStartAuthFlowRequest()), "application/json")
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringValue("status", Status.AUTHORIZING.getStatus())
                        .object("authorization_flow")
                        .object("actions")
                        .object("next")
                        .stringValue("type", AuthorizationFlowAction.Type.REDIRECT.getType())
                        .stringMatcher("uri", URI_REGEX, RETURN_URI)
                        .closeObject()
                        .closeObject()
                        .closeObject())
                .toPact();
    }

    @SneakyThrows
    @Override
    @DisplayName("It should authorize a payment")
    @PactTestFor(providerName = PROVIDER_NAME)
    @Test
    public void test() {
        ApiResponse<AuthorizationFlowResponse> authorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(A_PAYMENT_ID, buildStartAuthFlowRequest())
                .get();
        assertNotError(authorizationFlowResponse);

        ApiResponse<AuthorizationFlowResponse> authorizationFlowResponse2 = tlClient.payments()
                .startAuthorizationFlow(A_PAYMENT_ID, buildStartAuthFlowRequest())
                .get();
        assertNotError(authorizationFlowResponse2);
    }
}
