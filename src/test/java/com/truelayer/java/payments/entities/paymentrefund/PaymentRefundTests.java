package com.truelayer.java.payments.entities.paymentrefund;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import java.time.Clock;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PaymentRefundTests {

    @Test
    @DisplayName("It should yield true if instance is of type PendingPaymentRefund")
    public void shouldYieldTrueIfPendingPaymentRefund() {
        PaymentRefund sut = new PendingPaymentRefund();

        assertTrue(sut.isPending());
    }

    @Test
    @DisplayName("It should convert to an instance of class PendingPaymentRefund")
    public void shouldConvertToPendingPaymentRefund() {
        PaymentRefund sut = new PendingPaymentRefund();

        assertDoesNotThrow(sut::asPendingPaymentRefund);
    }

    @Test
    @DisplayName("It should throw an error when converting to PendingPaymentRefund")
    public void shouldNotConvertToPendingPaymentRefund() {
        PaymentRefund sut = new AuthorizedPaymentRefund();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPendingPaymentRefund);

        assertEquals(
                String.format("Payment refund is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type AuthorizedPaymentRefund")
    public void shouldYieldTrueIfAuthorizedPaymentRefund() {
        PaymentRefund sut = new AuthorizedPaymentRefund();

        assertTrue(sut.isAuthorized());
    }

    @Test
    @DisplayName("It should convert to an instance of class AuthorizedPaymentRefund")
    public void shouldConvertToAuthorizedPaymentRefund() {
        PaymentRefund sut = new AuthorizedPaymentRefund();

        assertDoesNotThrow(sut::asAuthorizedPaymentRefund);
    }

    @Test
    @DisplayName("It should throw an error when converting to AuthorizedPaymentRefund")
    public void shouldNotConvertToAuthorizedPaymentRefund() {
        PaymentRefund sut = new PendingPaymentRefund();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizedPaymentRefund);

        assertEquals(
                String.format("Payment refund is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type ExecutedPaymentRefund")
    public void shouldYieldTrueIfExecutedPaymentRefund() {
        PaymentRefund sut = new ExecutedPaymentRefund(ZonedDateTime.now(Clock.systemUTC()));

        assertTrue(sut.isExecuted());
    }

    @Test
    @DisplayName("It should convert to an instance of class ExecutedPaymentRefund")
    public void shouldConvertToExecutedPaymentRefund() {
        PaymentRefund sut = new ExecutedPaymentRefund(ZonedDateTime.now(Clock.systemUTC()));

        assertDoesNotThrow(sut::asExecutedPaymentRefund);
    }

    @Test
    @DisplayName("It should throw an error when converting to ExecutedPaymentRefund")
    public void shouldNotConvertToExecutedPaymentRefund() {
        PaymentRefund sut = new PendingPaymentRefund();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asExecutedPaymentRefund);

        assertEquals(
                String.format("Payment refund is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type FailedPaymentRefund")
    public void shouldYieldTrueIfFailedPaymentRefund() {
        PaymentRefund sut = new FailedPaymentRefund(ZonedDateTime.now(Clock.systemUTC()), "a failure reason");

        assertTrue(sut.isFailed());
    }

    @Test
    @DisplayName("It should convert to an instance of class FailedPaymentRefund")
    public void shouldConvertToFailedPaymentRefund() {
        PaymentRefund sut = new FailedPaymentRefund(ZonedDateTime.now(Clock.systemUTC()), "a failure reason");

        assertDoesNotThrow(sut::asFailedPaymentRefund);
    }

    @Test
    @DisplayName("It should throw an error when converting to FailedPaymentRefund")
    public void shouldNotConvertToFailedPaymentRefund() {
        PaymentRefund sut = new PendingPaymentRefund();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asFailedPaymentRefund);

        assertEquals(
                String.format("Payment refund is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
