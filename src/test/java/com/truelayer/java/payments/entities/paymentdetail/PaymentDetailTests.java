package com.truelayer.java.payments.entities.paymentdetail;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import java.time.Clock;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentDetailTests {

    @Test
    @DisplayName("It should yield true if instance is of type AuthorizationRequiredPaymentDetail")
    public void shouldYieldTrueIfAuthorizationRequiredPaymentDetail() {
        PaymentDetail sut = new AuthorizationRequiredPaymentDetail();

        assertTrue(sut.isAuthorizationRequired());
    }

    @Test
    @DisplayName("It should convert to an instance of class AuthorizationRequiredPaymentDetail")
    public void shouldConvertToAuthorizationRequiredPaymentDetail() {
        PaymentDetail sut = new AuthorizationRequiredPaymentDetail();

        assertDoesNotThrow(sut::asAuthorizationRequired);
    }

    @Test
    @DisplayName("It should throw an error when converting to AuthorizationRequiredPaymentDetail")
    public void shouldNotConvertToAuthorizationRequiredPaymentDetail() {
        PaymentDetail sut = new AuthorizingPaymentDetail(new AuthorizationFlowWithConfiguration(null));

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizationRequired);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type AuthorizingPaymentDetail")
    public void shouldYieldTrueIfAuthorizingPaymentDetail() {
        PaymentDetail sut = new AuthorizingPaymentDetail(new AuthorizationFlowWithConfiguration(null));

        assertTrue(sut.isAuthorizing());
    }

    @Test
    @DisplayName("It should convert to an instance of class AuthorizingPaymentDetail")
    public void shouldConvertToAuthorizingPaymentDetail() {
        PaymentDetail sut = new AuthorizingPaymentDetail(new AuthorizationFlowWithConfiguration(null));

        assertDoesNotThrow(sut::asAuthorizing);
    }

    @Test
    @DisplayName("It should throw an error when converting to AuthorizingPaymentDetail")
    public void shouldNotConvertToAuthorizingPaymentDetail() {
        PaymentDetail sut = new AuthorizationRequiredPaymentDetail();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorizing);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type AuthorizedPaymentDetail")
    public void shouldYieldTrueIfAuthorizedPaymentDetail() {
        PaymentDetail sut = new AuthorizedPaymentDetail(null, new AuthorizationFlowWithConfiguration(null));

        assertTrue(sut.isAuthorized());
    }

    @Test
    @DisplayName("It should convert to an instance of class AuthorizedPaymentDetail")
    public void shouldConvertToAuthorizedPaymentDetail() {
        PaymentDetail sut = new AuthorizedPaymentDetail(null, new AuthorizationFlowWithConfiguration(null));

        assertDoesNotThrow(sut::asAuthorized);
    }

    @Test
    @DisplayName("It should throw an error when converting to AuthorizingPaymentDetail")
    public void shouldNotConvertToAuthorizedPaymentDetail() {
        PaymentDetail sut = new AuthorizationRequiredPaymentDetail();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAuthorized);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type FailedPaymentDetail")
    public void shouldYieldTrueIfFailedPaymentDetail() {
        PaymentDetail sut = new FailedPaymentDetail(
                null,
                ZonedDateTime.now(Clock.systemUTC()),
                FailedPaymentDetail.FailureStage.AUTHORIZATION_REQUIRED,
                "failed for some reason",
                null);

        assertTrue(sut.isFailed());
    }

    @Test
    @DisplayName("It should convert to an instance of class FailedPaymentDetail")
    public void shouldConvertToFailedPaymentDetail() {
        PaymentDetail sut = new FailedPaymentDetail(
                null,
                ZonedDateTime.now(Clock.systemUTC()),
                FailedPaymentDetail.FailureStage.AUTHORIZATION_REQUIRED,
                "failed for some reason",
                null);

        assertDoesNotThrow(sut::asFailed);
    }

    @Test
    @DisplayName("It should throw an error when converting to FailedPaymentDetail")
    public void shouldNotConvertToFailedPaymentDetail() {
        PaymentDetail sut = new AuthorizationRequiredPaymentDetail();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asFailed);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type SettledPaymentDetail")
    public void shouldYieldTrueIfSettledPaymentDetail() {
        PaymentDetail sut = new SettledPaymentDetail(
                null,
                ZonedDateTime.now(Clock.systemUTC()),
                ZonedDateTime.now(Clock.systemUTC()),
                ZonedDateTime.now(Clock.systemUTC()),
                null,
                null);

        assertTrue(sut.isSettled());
    }

    @Test
    @DisplayName("It should convert to an instance of class SettledPaymentDetail")
    public void shouldConvertToSettledPaymentDetail() {
        PaymentDetail sut = new SettledPaymentDetail(
                null,
                ZonedDateTime.now(Clock.systemUTC()),
                ZonedDateTime.now(Clock.systemUTC()),
                ZonedDateTime.now(Clock.systemUTC()),
                null,
                null);

        assertDoesNotThrow(sut::asSettled);
    }

    @Test
    @DisplayName("It should throw an error when converting to SettledPaymentDetail")
    public void shouldNotConvertToSettledPaymentDetail() {
        PaymentDetail sut = new AuthorizationRequiredPaymentDetail();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asSettled);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type ExecutedPaymentDetail")
    public void shouldYieldTrueIfExecutedPaymentDetail() {
        PaymentDetail sut = new ExecutedPaymentDetail(null, ZonedDateTime.now(Clock.systemUTC()), null, null);

        assertTrue(sut.isExecuted());
    }

    @Test
    @DisplayName("It should convert to an instance of class ExecutedPaymentDetail")
    public void shouldConvertToExecutedPaymentDetail() {
        PaymentDetail sut = new ExecutedPaymentDetail(null, ZonedDateTime.now(Clock.systemUTC()), null, null);

        assertDoesNotThrow(sut::asExecuted);
    }

    @Test
    @DisplayName("It should throw an error when converting to ExecutedPaymentDetail")
    public void shouldNotConvertToExecutedPaymentDetail() {
        PaymentDetail sut = new AuthorizationRequiredPaymentDetail();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asExecuted);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type AttemptFailedPaymentDetail")
    public void shouldYieldTrueIfAttemptFailedPaymentDetail() {
        PaymentDetail sut = new AttemptFailedPaymentDetail(
                null,
                ZonedDateTime.now(Clock.systemUTC()),
                AttemptFailedPaymentDetail.FailureStage.AUTHORIZING,
                "failed for some reason",
                null);

        assertTrue(sut.isAttemptFailed());
    }

    @Test
    @DisplayName("It should convert to an instance of class AttemptFailedPaymentDetail")
    public void shouldConvertToAttemptFailedPaymentDetail() {
        PaymentDetail sut = new AttemptFailedPaymentDetail(
                null,
                ZonedDateTime.now(Clock.systemUTC()),
                AttemptFailedPaymentDetail.FailureStage.AUTHORIZING,
                "failed for some reason",
                null);

        assertDoesNotThrow(sut::asAttemptFailed);
    }

    @Test
    @DisplayName("It should throw an error when converting to AttemptFailedPaymentDetail")
    public void shouldNotConvertToAttemptFailedPaymentDetail() {
        PaymentDetail sut = new AuthorizationRequiredPaymentDetail();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asAttemptFailed);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
