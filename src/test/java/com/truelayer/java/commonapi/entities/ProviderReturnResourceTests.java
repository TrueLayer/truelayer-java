package com.truelayer.java.commonapi.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProviderReturnResourceTests {

    @Test
    @DisplayName("It should yield true if instance is of type PaymentProviderReturnResource")
    public void shouldYieldTrueIfPaymentProviderReturnResource() {
        ProviderReturnResource sut =
                new PaymentProviderReturnResource(UUID.randomUUID().toString());

        assertTrue(sut.isPaymentProviderReturnResponse());
    }

    @Test
    @DisplayName("It should convert to an instance of class PaymentProviderReturnResource")
    public void shouldConvertToPaymentProviderReturnResource() {
        ProviderReturnResource sut =
                new PaymentProviderReturnResource(UUID.randomUUID().toString());

        assertDoesNotThrow(sut::asPaymentProviderReturnResponse);
    }

    @Test
    @DisplayName("It should throw an error when converting to PaymentProviderReturnResource")
    public void shouldNotConvertToPaymentProviderReturnResource() {
        ProviderReturnResource sut =
                new MandateProviderReturnResource(UUID.randomUUID().toString());

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPaymentProviderReturnResponse);

        assertEquals(
                String.format(
                        "Provider return resource is of type %s.",
                        sut.getClass().getSimpleName()),
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type MandateProviderReturnResource")
    public void shouldYieldTrueIfMandateProviderReturnResource() {
        ProviderReturnResource sut =
                new MandateProviderReturnResource(UUID.randomUUID().toString());

        assertTrue(sut.isMandateProviderReturnResponse());
    }

    @Test
    @DisplayName("It should convert to an instance of class MandateProviderReturnResource")
    public void shouldConvertToMandateProviderReturnResource() {
        ProviderReturnResource sut =
                new MandateProviderReturnResource(UUID.randomUUID().toString());

        assertDoesNotThrow(sut::asMandateProviderReturnResponse);
    }

    @Test
    @DisplayName("It should throw an error when converting to MandateProviderReturnResource")
    public void shouldNotConvertToMandateProviderReturnResource() {
        ProviderReturnResource sut =
                new PaymentProviderReturnResource(UUID.randomUUID().toString());

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asMandateProviderReturnResponse);

        assertEquals(
                String.format(
                        "Provider return resource is of type %s.",
                        sut.getClass().getSimpleName()),
                thrown.getMessage());
    }
}
