package truelayer.java.payments.entities.paymentdetail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TrueLayerException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasePaymentDetailTests {

    @Test
    @DisplayName("It should throw an exception if a user tries to cast a payment to an unexpected state")
    public void shouldGetAFailedPaymentDetail() {
        BasePaymentDetail p = new SettledPaymentDetail();

        var thrown = Assertions.assertThrows(TrueLayerException.class, () -> p.asAuthorizingPaymentDetail());
        assertEquals("payment is of type SettledPaymentDetail. Consider using asSettledPaymentDetail() instead.", thrown.getMessage());
    }

    @Test
    @DisplayName("It should cast to a FailedPaymentDetail")
    public void shouldThrowAnException() {
        BasePaymentDetail payment = new FailedPaymentDetail();

        var failedPayment = payment.asFailedPaymentDetail();
        assertEquals(Status.FAILED, failedPayment.getStatus());
    }
}