package truelayer.java.payments.entities.paymentdetail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static truelayer.java.payments.entities.paymentdetail.FailedPaymentDetail.FailureStage.AUTHORIZATION_REQUIRED;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TrueLayerException;
import truelayer.java.payments.entities.paymentdetail.schemeidentifier.Iban;
import truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

class PaymentDetailTests {

    @Test
    @DisplayName("It should throw an exception if a user tries to cast a payment to an unexpected state")
    public void shouldGetAFailedPaymentDetail() {
        PaymentDetail p = new SettledPaymentDetail(
                new ExternalAccount(Arrays.asList(new Iban("123456")),
                        "ext-account-id",
                        "account name"),
                new Date(LocalDate.now().toEpochDay()),
                new Date(LocalDate.now().toEpochDay()),
                new Date(LocalDate.now().toEpochDay()),
                Optional.empty()
        );

        Throwable thrown = Assertions.assertThrows(TrueLayerException.class, () -> p.asAuthorizingPaymentDetail());
        assertEquals(
                "payment is of type SettledPaymentDetail. Consider using asSettledPaymentDetail() instead.",
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should cast to a FailedPaymentDetail")
    public void shouldThrowAnException() {
        PaymentDetail payment = new FailedPaymentDetail(
                new Date(LocalDate.now().toEpochDay()),
                AUTHORIZATION_REQUIRED,
                "whatever reason",
                Optional.empty()
        );

        FailedPaymentDetail failedPayment = payment.asFailedPaymentDetail();
        assertEquals(Status.FAILED, failedPayment.getStatus());
    }
}
