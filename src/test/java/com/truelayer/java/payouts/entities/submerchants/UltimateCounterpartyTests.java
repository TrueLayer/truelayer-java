package com.truelayer.java.payouts.entities.submerchants;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UltimateCounterpartyTests {

    @Test
    @DisplayName("It should yield true if instance is of type BusinessClient")
    public void shouldYieldTrueIfBusinessClient() {
        UltimateCounterparty sut =
                new BusinessClient.BusinessClientBuilder().tradingName("a-name").build();

        assertTrue(sut.isBusinessClient());
    }

    @Test
    @DisplayName("It should convert to an instance of class BusinessClient")
    public void shouldConvertToBusinessClient() {
        UltimateCounterparty sut =
                new BusinessClient.BusinessClientBuilder().tradingName("a-name").build();

        assertDoesNotThrow(sut::asBusinessClient);
    }
}
