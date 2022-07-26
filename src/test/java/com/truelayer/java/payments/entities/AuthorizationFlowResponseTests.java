package com.truelayer.java.payments.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorizationFlowResponseTests {

    @Test
    @DisplayName("It should yield true if instance is of type PaymentAuthorizationFlowAuthorizing")
    public void shouldYieldTrueIfAuthorizing() {
        AuthorizationFlowResponse sut = new AuthorizationFlowAuthorizing(new AuthorizationFlow());

        assertTrue(sut.isAuthorizing());
    }

    @Test
    @DisplayName("It should convert to an instance of class PaymentAuthorizationFlowAuthorizing")
    public void shouldConvertToAuthorizing() {
        AuthorizationFlowResponse sut = new AuthorizationFlowAuthorizing(new AuthorizationFlow());

        assertDoesNotThrow(sut::asAuthorizing);
    }

    @Test
    @DisplayName("It should throw an error when converting to PaymentAuthorizationFlowAuthorizing")
    public void shouldNotConvertToAuthorizing() {
        AuthorizationFlowResponse sut = new AuthorizationFlowAuthorizationFailed("some stage", "some reason");

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizing);

        assertEquals(String.format("Response is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type PaymentAuthorizationFlowAuthorizationFailed")
    public void shouldYieldTrueIfAuthorizationFailed() {
        AuthorizationFlowResponse sut = new AuthorizationFlowAuthorizationFailed("some stage", "some reason");

        assertTrue(sut.isAuthorizationFailed());
    }

    @Test
    @DisplayName("It should convert to an instance of class PaymentAuthorizationFlowAuthorizationFailed")
    public void shouldConvertToAuthorizationFailed() {
        AuthorizationFlowResponse sut = new AuthorizationFlowAuthorizationFailed("some stage", "some reason");

        assertDoesNotThrow(sut::asAuthorizationFailed);
    }

    @Test
    @DisplayName("It should throw an error when converting to PaymentAuthorizationFlowAuthorizationFailed")
    public void shouldNotConvertToAuthorizationFailed() {
        AuthorizationFlowResponse sut = new AuthorizationFlowAuthorizing(new AuthorizationFlow());

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizationFailed);

        assertEquals(String.format("Response is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
