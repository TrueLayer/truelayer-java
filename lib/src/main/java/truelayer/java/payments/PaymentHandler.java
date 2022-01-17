package truelayer.java.payments;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.TrueLayerException;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.GetPaymentByIdResponse;

import java.io.IOException;

@Builder
@Getter
public class PaymentHandler implements IPaymentHandler {
    private final IPaymentsApi paymentsApi;

    @Override
    public ApiResponse<CreatePaymentResponse> createPayment(CreatePaymentRequest createPaymentRequest) {
        try {
            return (ApiResponse<CreatePaymentResponse>) paymentsApi.createPayment(
                            createPaymentRequest)
                    .execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to create payment", e);
        }
    }

    @Override
    public ApiResponse<GetPaymentByIdResponse> getPayment(String paymentId) {
        try {
            return (ApiResponse<GetPaymentByIdResponse>) paymentsApi.getPayment(
                    paymentId
            ).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get payment", e);
        }
    }
}
