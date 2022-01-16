package truelayer.java.payments;

import retrofit2.http.*;
import truelayer.java.http.adapters.ApiCall;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.*;

public interface IPaymentsApi {

    @POST("/payments")
    ApiCall<ApiResponse<Payment>> createPayment(
            @Header("Authorization") String authorization,
            @Body CreatePaymentRequest body);

    @GET("/payments/{id}")
    ApiCall<ApiResponse<Payment>> getPayment(
            @Header("Authorization") String authorization,
            @Path("id") String paymentId);

    @POST("/payments/{id}/authorization-flow")
    ApiCall<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            @Header("Authorization") String authorization,
            @Path("id") String paymentId,
            @Body StartAuthorizationFlowRequest startAuthorizationFlowRequest);

    @POST("/payments/{id}/authorization-flow/actions/provider-selection")
    ApiCall<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            @Header("Authorization") String authorization,
            @Path("id") String paymentId,
            @Body SubmitProviderSelectionRequest submitProviderSelectionRequest);
}
