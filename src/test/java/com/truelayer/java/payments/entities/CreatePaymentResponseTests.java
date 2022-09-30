package com.truelayer.java.payments.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreatePaymentResponseTests {

    @Test
    @DisplayName("It should yield true if instance is of type CreatePaymentResponseAuthorizationRequired")
    public void shouldYieldTrueIfAuthorizationRequired() {
        CreatePaymentResponse sut = new CreatePaymentResponseAuthorizationRequired();

        assertTrue(sut.isAuthorizationRequired());
    }

    @Test
    @DisplayName("It should convert to an instance of class CreatePaymentResponseAuthorizationRequired")
    public void shouldConvertToAuthorizationRequired() {
        CreatePaymentResponse sut = new CreatePaymentResponseAuthorizationRequired();

        assertDoesNotThrow(sut::asAuthorizationRequired);
    }

    @Test
    @DisplayName("It should throw an error when converting to CreatePaymentResponseAuthorizationRequired")
    public void shouldNotConvertToAuthorizationRequired() {
        CreatePaymentResponse sut = new CreatePaymentResponseFailed(null, null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizationRequired);

        assertEquals(String.format("Response is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type CreatePaymentResponseAuthorized")
    public void shouldYieldTrueIfAuthorized() {
        CreatePaymentResponse sut = new CreatePaymentResponseAuthorized();

        assertTrue(sut.isAuthorized());
    }

    @Test
    @DisplayName("It should convert to an instance of class CreatePaymentResponseAuthorized")
    public void shouldConvertToAuthorized() {
        CreatePaymentResponse sut = new CreatePaymentResponseAuthorized();

        assertDoesNotThrow(sut::asAuthorized);
    }

    @Test
    @DisplayName("It should throw an error when converting to CreatePaymentResponseAuthorized")
    public void shouldNotConvertToAuthorized() {
        CreatePaymentResponse sut = new CreatePaymentResponseFailed(null, null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorized);

        assertEquals(String.format("Response is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type CreatePaymentResponseFailed")
    public void shouldYieldTrueIfFailed() {
        CreatePaymentResponse sut = new CreatePaymentResponseFailed("some stage", "some reason");

        assertTrue(sut.isFailed());
    }

    @Test
    @DisplayName("It should convert to an instance of class CreatePaymentResponseFailed")
    public void shouldConvertToFailed() {
        CreatePaymentResponse sut = new CreatePaymentResponseFailed("some stage", "some reason");

        assertDoesNotThrow(sut::asFailed);
    }

    @Test
    @DisplayName("It should throw an error when converting to CreatePaymentResponseFailed")
    public void shouldNotConvertToFailed() {
        CreatePaymentResponse sut = new CreatePaymentResponseAuthorized();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asFailed);

        assertEquals(String.format("Response is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
