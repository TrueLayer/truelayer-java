package truelayer.java.payments;

import java.io.IOException;
import lombok.Value;
import truelayer.java.TrueLayerException;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.paymentdetail.BasePaymentDetail;

@Value
public class PaymentHandler implements IPaymentHandler {
    IPaymentsApi paymentsApi;

    public static PaymentHandlerBuilder New() {
        return new PaymentHandlerBuilder();
    }

    @Override
    public ApiResponse<CreatePaymentResponse> createPayment(CreatePaymentRequest createPaymentRequest) {
        try {
            return (ApiResponse<CreatePaymentResponse>)
                    paymentsApi.createPayment(createPaymentRequest).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to create payment", e);
        }
    }

    @Override
    public ApiResponse<BasePaymentDetail> getPayment(String paymentId) {
        try {
            return (ApiResponse<BasePaymentDetail>)
                    paymentsApi.getPayment(paymentId).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get payment", e);
        }
    }
}
