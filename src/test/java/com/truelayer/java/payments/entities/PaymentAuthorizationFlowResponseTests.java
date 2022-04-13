package com.truelayer.java.payments.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentAuthorizationFlowResponseTests {

    @Test
    @DisplayName("It should yield true if instance is of type PaymentAuthorizationFlowAuthorizing")
    public void shouldYieldTrueIfAuthorizing() {
        PaymentAuthorizationFlowResponse sut = new PaymentAuthorizationFlowAuthorizing();

        assertTrue(sut.isAuthorizing());
    }

    @Test
    @DisplayName("It should convert to an instance of class PaymentAuthorizationFlowAuthorizing")
    public void shouldConvertToAuthorizing() {
        PaymentAuthorizationFlowResponse sut = new PaymentAuthorizationFlowAuthorizing();

        assertDoesNotThrow(sut::asAuthorizing);
    }

    @Test
    @DisplayName("It should throw an error when converting to PaymentAuthorizationFlowAuthorizing")
    public void shouldNotConvertToAuthorizing() {
        PaymentAuthorizationFlowResponse sut =
                new PaymentAuthorizationFlowAuthorizationFailed("some stage", "some reason");

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizing);

        assertEquals(String.format("Response is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type PaymentAuthorizationFlowAuthorizationFailed")
    public void shouldYieldTrueIfAuthorizationFailed() {
        PaymentAuthorizationFlowResponse sut =
                new PaymentAuthorizationFlowAuthorizationFailed("some stage", "some reason");

        assertTrue(sut.isAuthorizationFailed());
    }

    @Test
    @DisplayName("It should convert to an instance of class PaymentAuthorizationFlowAuthorizationFailed")
    public void shouldConvertToAuthorizationFailed() {
        PaymentAuthorizationFlowResponse sut =
                new PaymentAuthorizationFlowAuthorizationFailed("some stage", "some reason");

        assertDoesNotThrow(sut::asAuthorizationFailed);
    }

    @Test
    @DisplayName("It should throw an error when converting to PaymentAuthorizationFlowAuthorizationFailed")
    public void shouldNotConvertToAuthorizationFailed() {
        PaymentAuthorizationFlowResponse sut = new PaymentAuthorizationFlowAuthorizing();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizationFailed);

        assertEquals(String.format("Response is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
