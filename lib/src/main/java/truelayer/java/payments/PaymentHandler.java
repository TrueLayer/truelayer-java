package truelayer.java.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import truelayer.java.SigningOptions;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.signing.Signer;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

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

    private String buildAuthorizationHeader(String token) {
        return new StringBuilder("Bearer").append(" ").append(token).toString();
    }
}
