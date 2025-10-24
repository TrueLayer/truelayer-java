package com.truelayer.java.payouts.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreatePayoutResponseTests {

    @Test
    @DisplayName("It should yield true if instance is of type CreatedPayout")
    public void shouldYieldTrueIfCreatedPayout() {
        CreatePayoutResponse sut = new CreatedPayout();

        assertTrue(sut.isCreated());
    }

    @Test
    @DisplayName("It should convert to an instance of class CreatedPayout")
    public void shouldConvertToCreatedPayout() {
        CreatePayoutResponse sut = new CreatedPayout();

        assertDoesNotThrow(sut::asCreated);
    }

    @Test
    @DisplayName("It should throw an error when converting to CreatedPayout")
    public void shouldNotConvertToCreatedPayout() {
        CreatePayoutResponse sut = new AuthorizationRequiredPayout();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asCreated);

        assertEquals(
                String.format(
                        "Create payout response is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type AuthorizationRequiredPayout")
    public void shouldYieldTrueIfAuthorizationRequiredPayout() {
        CreatePayoutResponse sut = new AuthorizationRequiredPayout();

        assertTrue(sut.isAuthorizationRequired());
    }

    @Test
    @DisplayName("It should convert to an instance of class AuthorizationRequiredPayout")
    public void shouldConvertToAuthorizationRequiredPayout() {
        CreatePayoutResponse sut = new AuthorizationRequiredPayout();

        assertDoesNotThrow(sut::asAuthorizationRequired);
    }

    @Test
    @DisplayName("It should throw an error when converting to AuthorizationRequiredPayout")
    public void shouldNotConvertToAuthorizationRequiredPayout() {
        CreatePayoutResponse sut = new CreatedPayout();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizationRequired);

        assertEquals(
                String.format(
                        "Create payout response is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }
}
