package truelayer.java.payments;

import retrofit2.http.*;
import truelayer.java.http.adapters.ApiCall;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

public interface IPaymentsApi {

    @POST("/payments")
    ApiCall<ApiResponse<Payment>> createPayment(
            @Header("Authorization") String authorization,
            @Body CreatePaymentRequest body);

    @GET("/payments/{id}")
    ApiCall<ApiResponse<Payment>> getPayment(
            @Header("Authorization") String authorization,
            @Path("id") String paymentId);
}
