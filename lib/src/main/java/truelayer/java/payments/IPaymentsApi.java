package truelayer.java.payments;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.*;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;

public interface IPaymentsApi {

    @POST("/payments")
    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(@Body CreatePaymentRequest body);

    @GET("/payments/{id}")
    CompletableFuture<ApiResponse<PaymentDetail>> getPayment(@Path("id") String paymentId);

    @POST("/payments/{id}/authorization-flow")
    CompletableFuture<ApiResponse<StartAuthorizationFlowResponse>> startAuthorizationFlow(
            @Path("id") String paymentId, @Body StartAuthorizationFlowRequest body);

    @POST("/payments/{id}/authorization-flow/actions/provider-selection")
    CompletableFuture<ApiResponse<SubmitProviderSelectionResponse>> submitProviderSelection(
            @Path("id") String paymentId, String providerId);
}
