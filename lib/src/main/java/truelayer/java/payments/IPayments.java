package truelayer.java.payments;

import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

public interface IPayments {

    Payment createPayment(CreatePaymentRequest request);
}
