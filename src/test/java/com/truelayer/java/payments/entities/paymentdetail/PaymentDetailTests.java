package com.truelayer.java.payments.entities.paymentdetail;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.PaymentSource;
import com.truelayer.java.entities.accountidentifier.IbanAccountIdentifier;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentDetailTests {

    @Test
    @DisplayName("It should throw an exception if a user tries to cast a payment to an unexpected state")
    public void shouldGetAFailedPaymentDetail() {
        PaymentDetail p = new SettledPaymentDetail(
                new PaymentSource(
                        UUID.randomUUID().toString(),
                        Collections.singletonList(
                                IbanAccountIdentifier.builder().iban("123456").build()),
                        "account name"),
                new Date(LocalDate.now().toEpochDay()),
                new Date(LocalDate.now().toEpochDay()),
                new Date(LocalDate.now().toEpochDay()),
                null);

        Throwable thrown = Assertions.assertThrows(TrueLayerException.class, p::asAuthorizingPaymentDetail);
        assertEquals(
                "payment is of type SettledPaymentDetail. Consider using asSettledPaymentDetail() instead.",
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should cast to a FailedPaymentDetail")
    public void shouldThrowAnException() {
        PaymentDetail payment = new FailedPaymentDetail(
                new Date(LocalDate.now().toEpochDay()),
                FailedPaymentDetail.FailureStage.AUTHORIZATION_REQUIRED,
                "whatever reason",
                null);

        FailedPaymentDetail failedPayment = payment.asFailedPaymentDetail();
        assertEquals(Status.FAILED, failedPayment.getStatus());
    }
}