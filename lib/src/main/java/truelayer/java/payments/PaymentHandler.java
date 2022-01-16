package truelayer.java.payments;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.*;

import java.io.IOException;
import java.util.Arrays;

@Builder
@Getter
public class PaymentHandler implements IPaymentHandler {

    private IAuthenticationHandler authenticationHandler;
    private IPaymentsApi paymentsApi;
    private String[] paymentsScopes;

    @Override
    public ApiResponse<Payment> createPayment(CreatePaymentRequest createPaymentRequest) {
        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes));
        try {
            return (ApiResponse<Payment>) paymentsApi.createPayment(
                            buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                            createPaymentRequest)
                    .execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to create payment", e);
        }
    }

    @Override
    public ApiResponse<Payment> getPayment(String paymentId) {
        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes));
        try {
            return (ApiResponse<Payment>) paymentsApi.getPayment(
                    buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                    paymentId
            ).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get payment", e);
        }
    }

    @Override
    public ApiResponse<AuthorizationFlowResponse> startAuthorizationFlow(String paymentId, StartAuthorizationFlowRequest startAuthorizationFlowRequest) {
        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes));
        try {
            return (ApiResponse<AuthorizationFlowResponse>) paymentsApi.startAuthorizationFlow(
                    buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                    paymentId,
                    startAuthorizationFlowRequest
            ).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to start authorization flow", e);
        }
    }

    @Override
    public ApiResponse<AuthorizationFlowResponse> submitProviderSelection(String paymentId, SubmitProviderSelectionRequest submitProviderSelectionRequest) {
        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes));
        try {
            return (ApiResponse<AuthorizationFlowResponse>) paymentsApi.submitProviderSelection(
                    buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                    paymentId,
                    submitProviderSelectionRequest
            ).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to submit provider selection", e);
        }
    }

    private String buildAuthorizationHeader(String token) {
        return new StringBuilder("Bearer").append(" ").append(token).toString();
    }
}
