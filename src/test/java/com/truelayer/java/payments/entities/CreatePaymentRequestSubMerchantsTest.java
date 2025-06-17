package com.truelayer.java.payments.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.User;
import com.truelayer.java.payments.entities.paymentmethod.BankTransfer;
import com.truelayer.java.payments.entities.submerchants.BusinessClient;
import com.truelayer.java.payments.entities.submerchants.SubMerchants;
import com.truelayer.java.payments.entities.submerchants.UltimateCounterparty;
import org.junit.jupiter.api.Test;

class CreatePaymentRequestSubMerchantsTest {

    @Test
    void canCreatePaymentRequestWithSubMerchants() {
        BusinessClient businessClient = UltimateCounterparty.businessClient()
                .tradingName("Test Trading Name")
                .build();

        SubMerchants subMerchants =
                SubMerchants.builder().ultimateCounterparty(businessClient).build();

        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .amountInMinor(1000)
                .currency(CurrencyCode.GBP)
                .paymentMethod(BankTransfer.builder().build())
                .user(User.builder().id("user-123").build())
                .subMerchants(subMerchants)
                .build();

        assertNotNull(request);
        assertNotNull(request.getSubMerchants());
        assertEquals(
                UltimateCounterparty.Type.BUSINESS_CLIENT,
                request.getSubMerchants().getUltimateCounterparty().getType());
    }
}
