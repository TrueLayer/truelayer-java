package truelayer.java.payments;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.paymentdetail.BasePaymentDetail;

public interface IPaymentsApi {

    @POST("/payments")
    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(@Body CreatePaymentRequest body);

    @GET("/payments/{id}")
    CompletableFuture<ApiResponse<BasePaymentDetail>> getPayment(@Path("id") String paymentId);
}
