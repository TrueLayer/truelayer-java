package truelayer.java.payments;

import retrofit2.http.*;
import truelayer.java.http.adapters.ApiCall;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.paymentdetail.BasePaymentDetail;

public interface IPaymentsApi {

    @POST("/payments")
    ApiCall<ApiResponse<CreatePaymentResponse>> createPayment(@Body CreatePaymentRequest body);

    @GET("/payments/{id}")
    ApiCall<ApiResponse<BasePaymentDetail>> getPayment(@Path("id") String paymentId);
}
