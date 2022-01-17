package truelayer.java.payments;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.TrueLayerException;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import java.io.IOException;

@Builder
@Getter
public class PaymentHandler implements IPaymentHandler {
    private final IPaymentsApi paymentsApi;

    @Override
    public ApiResponse<Payment> createPayment(CreatePaymentRequest createPaymentRequest) {
        try {
            return (ApiResponse<Payment>) paymentsApi.createPayment(createPaymentRequest).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to create payment", e);
        }
    }

    @Override
    public ApiResponse<Payment> getPayment(String paymentId) {
        try {
            return (ApiResponse<Payment>) paymentsApi.getPayment(paymentId).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get payment", e);
        }
    }
}
