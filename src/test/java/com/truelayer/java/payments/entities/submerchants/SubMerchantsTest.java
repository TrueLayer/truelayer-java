package com.truelayer.java.payments.entities.submerchants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.truelayer.java.entities.Address;
import org.junit.jupiter.api.Test;

class SubMerchantsTest {

    @Test
    void canCreateBusinessClient() {
        Address address = Address.builder()
                .addressLine1("123 Test Street")
                .city("London")
                .countryCode("GB")
                .build();

        BusinessClient businessClient = UltimateCounterparty.businessClient()
                .tradingName("Test Trading Name")
                .commercialName("Test Commercial Name")
                .url("https://example.com")
                .mcc("1234")
                .registrationNumber("REG123")
                .address(address)
                .build();

        assertNotNull(businessClient);
        assertEquals(UltimateCounterparty.Type.BUSINESS_CLIENT, businessClient.getType());
        assertEquals("Test Trading Name", businessClient.getTradingName());
    }

    @Test
    void canCreateBusinessDivision() {
        BusinessDivision businessDivision = UltimateCounterparty.businessDivision()
                .id("division-123")
                .name("Test Division")
                .build();

        assertNotNull(businessDivision);
        assertEquals(UltimateCounterparty.Type.BUSINESS_DIVISION, businessDivision.getType());
        assertEquals("division-123", businessDivision.getId());
        assertEquals("Test Division", businessDivision.getName());
    }

    @Test
    void canCreateSubMerchantsWithBusinessClient() {
        BusinessClient businessClient = UltimateCounterparty.businessClient()
                .tradingName("Test Trading Name")
                .build();

        SubMerchants subMerchants =
                SubMerchants.builder().ultimateCounterparty(businessClient).build();

        assertNotNull(subMerchants);
        assertNotNull(subMerchants.getUltimateCounterparty());
        assertEquals(
                UltimateCounterparty.Type.BUSINESS_CLIENT,
                subMerchants.getUltimateCounterparty().getType());
    }
}
