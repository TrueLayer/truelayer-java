package truelayer.java.payments;

import retrofit2.Call;
import retrofit2.http.*;
import truelayer.java.http.ApiResponse;
import truelayer.java.http.ApiResponseCallAdapter;
import truelayer.java.http.ApiResponseCallAdapter.ApiCall;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

public interface IPaymentsApi {

    @POST("/payments")
    ApiCall<ApiResponse<Payment>> createPayment(
            @Header("Idempotency-Key") String idempotencyKey,
            @Header("Tl-Signature") String signature,
            @Header("Authorization") String authorization,
            @Body CreatePaymentRequest body);

    @GET("/payments/{id}")
    ApiCall<ApiResponse<Payment>> getPayment(
            @Header("Authorization") String authorization,
            @Path("id") String paymentId);
}
