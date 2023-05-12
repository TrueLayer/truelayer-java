package com.truelayer.java.payouts.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import java.time.Clock;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PayoutTests {

    @Test
    @DisplayName("It should yield true if instance is of type PendingPayout")
    public void shouldYieldTrueIfPendingPayout() {
        Payout sut = new PendingPayout();

        assertTrue(sut.isPending());
    }

    @Test
    @DisplayName("It should convert to an instance of class PendingPayout")
    public void shouldConvertToPendingPayout() {
        Payout sut = new PendingPayout();

        assertDoesNotThrow(sut::asPendingPayout);
    }

    @Test
    @DisplayName("It should throw an error when converting to PendingPayout")
    public void shouldNotConvertToPendingPayout() {
        Payout sut = new AuthorizedPayout();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPendingPayout);

        assertEquals(String.format("Payout is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type AuthorizedPayout")
    public void shouldYieldTrueIfAuthorizedPayout() {
        Payout sut = new AuthorizedPayout();

        assertTrue(sut.isAuthorized());
    }

    @Test
    @DisplayName("It should convert to an instance of class AuthorizedPayout")
    public void shouldConvertToAuthorizedPayout() {
        Payout sut = new AuthorizedPayout();

        assertDoesNotThrow(sut::asAuthorizedPayout);
    }

    @Test
    @DisplayName("It should throw an error when converting to AuthorizedPayout")
    public void shouldNotConvertToAuthorizedPayout() {
        Payout sut = new PendingPayout();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizedPayout);

        assertEquals(String.format("Payout is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type ExecutedPayout")
    public void shouldYieldTrueIfExecutedPayout() {
        Payout sut = new ExecutedPayout(ZonedDateTime.now(Clock.systemUTC()));

        assertTrue(sut.isExecuted());
    }

    @Test
    @DisplayName("It should convert to an instance of class ExecutedPayout")
    public void shouldConvertToExecutedPayout() {
        Payout sut = new ExecutedPayout(ZonedDateTime.now(Clock.systemUTC()));

        assertDoesNotThrow(sut::asExecutedPayout);
    }

    @Test
    @DisplayName("It should throw an error when converting to ExecutedPayout")
    public void shouldNotConvertToExecutedPayout() {
        Payout sut = new PendingPayout();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asExecutedPayout);

        assertEquals(String.format("Payout is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type FailedPayout")
    public void shouldYieldTrueIfFailedPayout() {
        Payout sut = new FailedPayout(ZonedDateTime.now(Clock.systemUTC()), "a failure reason");

        assertTrue(sut.isFailed());
    }

    @Test
    @DisplayName("It should convert to an instance of class FailedPayout")
    public void shouldConvertToFailedPayout() {
        Payout sut = new FailedPayout(ZonedDateTime.now(Clock.systemUTC()), "a failure reason");

        assertDoesNotThrow(sut::asFailedPayout);
    }

    @Test
    @DisplayName("It should throw an error when converting to FailedPayout")
    public void shouldNotConvertToFailedPayout() {
        Payout sut = new PendingPayout();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asFailedPayout);

        assertEquals(String.format("Payout is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
