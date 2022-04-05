package com.truelayer.java.merchantaccounts.entities.transactions;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.CurrencyCode;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionTests {

    @Test
    @DisplayName("It should yield true if instance is of type MerchantAccountPayment")
    public void shouldYieldTrueIfMerchantAccountPayment() {
        Transaction sut = new MerchantAccountPayment(
                UUID.randomUUID().toString(),
                CurrencyCode.GBP,
                100,
                Transaction.Status.PENDING,
                new Date(),
                new PaymentSource(UUID.randomUUID().toString(), null, null, null),
                UUID.randomUUID().toString());

        assertTrue(sut.isMerchantAccountPayment());
    }

    @Test
    @DisplayName("It should convert to an instance of class MerchantAccountPayment")
    public void shouldConvertToMerchantAccountPayment() {
        Transaction sut = new MerchantAccountPayment(
                UUID.randomUUID().toString(),
                CurrencyCode.GBP,
                100,
                Transaction.Status.PENDING,
                new Date(),
                null,
                UUID.randomUUID().toString());

        assertDoesNotThrow(sut::asMerchantAccountPayment);
    }

    @Test
    @DisplayName("It should throw an error when converting to MerchantAccountPayment")
    public void shouldNotConvertToSortCodeAccNumber() {
        Transaction sut = new ExternalPayment(
                UUID.randomUUID().toString(), CurrencyCode.GBP, 100, Transaction.Status.PENDING, new Date(), null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asMerchantAccountPayment);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type ExternalPayment")
    public void shouldYieldTrueIfExternalPayment() {
        Transaction sut = new ExternalPayment(
                UUID.randomUUID().toString(), CurrencyCode.GBP, 100, Transaction.Status.PENDING, new Date(), null);

        assertTrue(sut.isExternalPayment());
    }

    @Test
    @DisplayName("It should convert to an instance of class ExternalPayment")
    public void shouldConvertToExternalPayment() {
        Transaction sut = new ExternalPayment(
                UUID.randomUUID().toString(), CurrencyCode.GBP, 100, Transaction.Status.PENDING, new Date(), null);

        assertDoesNotThrow(sut::asExternalPayment);
    }

    @Test
    @DisplayName("It should throw an error when converting to ExternalPayment")
    public void shouldNotConvertToExternalPayment() {
        Transaction sut = new MerchantAccountPayment(
                UUID.randomUUID().toString(),
                CurrencyCode.GBP,
                100,
                Transaction.Status.PENDING,
                new Date(),
                null,
                UUID.randomUUID().toString());

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asExternalPayment);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type Payout")
    public void shouldYieldTrueIfPayout() {
        Transaction sut = new Payout(
                UUID.randomUUID().toString(),
                CurrencyCode.GBP,
                100,
                Transaction.Status.PENDING,
                new Date(),
                new Date(),
                null,
                Payout.ContextCode.INTERNAL,
                UUID.randomUUID().toString());

        assertTrue(sut.isPayout());
    }

    @Test
    @DisplayName("It should convert to an instance of class Payout")
    public void shouldConvertToPayout() {
        Transaction sut = new Payout(
                UUID.randomUUID().toString(),
                CurrencyCode.GBP,
                100,
                Transaction.Status.PENDING,
                new Date(),
                new Date(),
                null,
                Payout.ContextCode.INTERNAL,
                UUID.randomUUID().toString());

        assertDoesNotThrow(sut::asPayout);
    }

    @Test
    @DisplayName("It should throw an error when converting to Payout")
    public void shouldNotConvertToPayout() {
        Transaction sut = new MerchantAccountPayment(
                UUID.randomUUID().toString(),
                CurrencyCode.GBP,
                100,
                Transaction.Status.PENDING,
                new Date(),
                null,
                UUID.randomUUID().toString());

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPayout);

        assertEquals(String.format("Payment is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
