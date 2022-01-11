package truelayer.java.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import truelayer.java.SigningOptions;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.signing.Signer;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Builder
@Getter
public class PaymentHandler implements IPaymentHandler {

    private IAuthenticationHandler authenticationHandler;
    private IPaymentsApi paymentsApi;
    private String[] paymentsScopes;

    private SigningOptions signingOptions;

    @Override
    @SneakyThrows //todo remove
    public ApiResponse<Payment> createPayment(CreatePaymentRequest createPaymentRequest) {
        var idempotencyKey = UUID.randomUUID().toString();
        var createRequestJsonString = requestToJsonString(createPaymentRequest);
        var signature = signRequest(idempotencyKey, createRequestJsonString, "/payments");

        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes)).get();
        try {
            var c = paymentsApi.createPayment(idempotencyKey,
                    signature,
                    buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                    createPaymentRequest);

            c.enqueue(new Callback<ApiResponse<Payment>>() {

                @Override
                public void onResponse(Call<ApiResponse<Payment>> call, Response<ApiResponse<Payment>> response) {

                }

                @Override
                public void onFailure(Call<ApiResponse<Payment>> call, Throwable t) {

                }
            });

            c.cancel();


            return (ApiResponse<Payment>) paymentsApi.createPayment(idempotencyKey,
                            signature,
                            buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                            createPaymentRequest)
                    .execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to create payment", e);
        }
    }

    @Override
    @SneakyThrows //todo remove
    public ApiResponse<Payment> getPayment(String paymentId) {
        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes)).get();
        try {
            return (ApiResponse<Payment>) paymentsApi.getPayment(
                    buildAuthorizationHeader(oauthToken.getData().getAccessToken()),
                    paymentId
            ).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get payment", e);
        }
    }


    private String signRequest(String idempotencyKey, String jsonRequest, String path) {
        byte[] privateKey = signingOptions.getPrivateKey();

        return new Signer.Builder(signingOptions.getKeyId(), privateKey)
                .addHeader("Idempotency-Key", idempotencyKey)
                .addHttpMethod("post")
                .addPath(path)
                .addBody(jsonRequest)
                .getSignature();
    }

    private String requestToJsonString(CreatePaymentRequest createPaymentRequest) {
        try {
            return new ObjectMapper().writeValueAsString(createPaymentRequest);
        } catch (JsonProcessingException e) {
            throw new TrueLayerException("unable to parse request as JSON");
        }
    }

    private String buildAuthorizationHeader(String token) {
        return new StringBuilder("Bearer").append(" ").append(token).toString();
    }
}
