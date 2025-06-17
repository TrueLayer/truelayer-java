package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.paymentdetail.ExecutedPaymentDetail;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.submerchants.BusinessClient;
import com.truelayer.java.payments.entities.submerchants.UltimateCounterparty;
import java.util.Collections;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentSubMerchantsIntegrationTest extends IntegrationTests {

    @Test
    @DisplayName("It should deserialize payment response with sub_merchants")
    @SneakyThrows
    void shouldDeserializePaymentWithSubMerchants() {
        String paymentId = "payment-id";

        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();

        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo(String.format("/payments/%s", paymentId)))
                .status(200)
                .bodyFile("payments/200.get_payment_by_id.with_submerchants.json")
                .build();

        ApiResponse<PaymentDetail> response =
                tlClient.payments().getPayment(paymentId).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);

        PaymentDetail result = response.getData();
        assertNotNull(result);
        assertTrue(result.isExecuted());

        ExecutedPaymentDetail executedPayment = result.asExecuted();
        assertNotNull(executedPayment.getSubMerchants());
        assertNotNull(executedPayment.getSubMerchants().getUltimateCounterparty());

        UltimateCounterparty ultimateCounterparty =
                executedPayment.getSubMerchants().getUltimateCounterparty();
        assertTrue(ultimateCounterparty.isBusinessClient());

        BusinessClient businessClient = ultimateCounterparty.asBusinessClient();
        assertEquals("Example Trading Ltd", businessClient.getTradingName());
        assertEquals("Example Commercial Name", businessClient.getCommercialName());
        assertEquals("https://example.com", businessClient.getUrl());
        assertEquals("1234", businessClient.getMcc());
        assertEquals("12345678", businessClient.getRegistrationNumber());
        assertNotNull(businessClient.getAddress());
        assertEquals("London", businessClient.getAddress().getCity());
    }

    @Test
    @DisplayName("It should handle payment response without sub_merchants (backward compatibility)")
    @SneakyThrows
    void shouldHandlePaymentWithoutSubMerchants() {
        String paymentId = "payment-id";

        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();

        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo(String.format("/payments/%s", paymentId)))
                .status(200)
                .bodyFile("payments/200.get_payment_by_id.bank_transfer.executed.json")
                .build();

        ApiResponse<PaymentDetail> response =
                tlClient.payments().getPayment(paymentId).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);

        PaymentDetail result = response.getData();
        assertNotNull(result);
        assertTrue(result.isExecuted());

        ExecutedPaymentDetail executedPayment = result.asExecuted();
        // subMerchants should be null for backward compatibility
        assertNull(executedPayment.getSubMerchants());
    }
}
