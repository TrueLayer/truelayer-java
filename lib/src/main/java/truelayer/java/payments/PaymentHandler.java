package truelayer.java.payments;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.GetPaymentByIdResponse;

import java.io.IOException;
import java.util.Arrays;

@Builder
@Getter
public class PaymentHandler implements IPaymentHandler {

    private IAuthenticationHandler authenticationHandler;
    private IPaymentsApi paymentsApi;
    private String[] paymentsScopes;

    @Override
    public ApiResponse<CreatePaymentResponse> createPayment(CreatePaymentRequest createPaymentRequest) {
        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes));
        try {
            return (ApiResponse<CreatePaymentResponse>) paymentsApi.createPayment(
                            buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                            createPaymentRequest)
                    .execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to create payment", e);
        }
    }

    @Override
    public ApiResponse<GetPaymentByIdResponse> getPayment(String paymentId) {
        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes));
        try {
            return (ApiResponse<GetPaymentByIdResponse>) paymentsApi.getPayment(
                    buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                    paymentId
            ).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get payment", e);
        }
    }

    private String buildAuthorizationHeader(String token) {
        return new StringBuilder("Bearer").append(" ").append(token).toString();
    }
}
