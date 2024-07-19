package com.truelayer.java.payments.entities.paymentmethod;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentMethodTests {

    @Test
    @DisplayName("It should yield true if instance is of type BankTransfer")
    public void shouldYieldTrueIfBankTransfer() {
        PaymentMethod sut = new BankTransfer(null, null, null);

        assertTrue(sut.isBankTransfer());
    }

    @Test
    @DisplayName("It should convert to an instance of class BankTransfer")
    public void shouldConvertToBankTransfer() {
        PaymentMethod sut = new BankTransfer(null, null, null);

        assertDoesNotThrow(sut::asBankTransfer);
    }

    @Test
    @DisplayName("It should throw an error when converting to BankTransfer")
    public void shouldNotConvertToBankTransfer() {
        PaymentMethod sut =
                Mandate.builder().mandateId(UUID.randomUUID().toString()).build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asBankTransfer);

        assertEquals(
                String.format("Payment method is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type Mandate")
    public void shouldYieldTrueIfMandate() {
        PaymentMethod sut =
                Mandate.builder().mandateId(UUID.randomUUID().toString()).build();

        assertTrue(sut.isMandate());
    }

    @Test
    @DisplayName("It should convert to an instance of class Mandate")
    public void shouldConvertToMandate() {
        PaymentMethod sut =
                Mandate.builder().mandateId(UUID.randomUUID().toString()).build();

        assertDoesNotThrow(sut::asMandate);
    }

    @Test
    @DisplayName("It should throw an error when converting to Mandate")
    public void shouldNotConvertToMandate() {
        PaymentMethod sut = new BankTransfer(null, null, null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asMandate);

        assertEquals(
                String.format("Payment method is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
