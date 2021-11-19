package truelayer.java.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import truelayer.java.SigningOptions;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.signing.Signer;

import java.text.ParseException;
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
    @SneakyThrows
    public Payment createPayment(CreatePaymentRequest createPaymentRequest) {
        var idempotencyKey = UUID.randomUUID().toString();
        var createRequestJsonString = requestToJsonString(createPaymentRequest);
        var signature = signRequest(idempotencyKey, createRequestJsonString, "/payments");

        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes));
        return paymentsApi.createPayment(idempotencyKey,
                        signature,
                        buildAuthorizationHeader(oauthToken.getAccessToken()),
                        createPaymentRequest)
                .execute().body();
    }

    @Override
    @SneakyThrows
    public Payment getPayment(String paymentId) {
        var oauthToken = authenticationHandler.getOauthToken(Arrays.asList(paymentsScopes));
        return paymentsApi.getPayment(
                buildAuthorizationHeader(oauthToken.getAccessToken()),
                paymentId
        ).execute().body();
    }


    private String signRequest(String idempotencyKey, String jsonRequest, String path) throws ParseException, JOSEException {
        byte[] privateKey = signingOptions.getPrivateKey();

        return new Signer.Builder(signingOptions.getKeyId(), privateKey)
                .addHeader("Idempotency-Key", idempotencyKey)
                .addHttpMethod("post")
                .addPath(path)
                .addBody(jsonRequest)
                .getSignature();
    }

    private String requestToJsonString(CreatePaymentRequest createPaymentRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createPaymentRequest);
    }

    private String buildAuthorizationHeader(String token) {
        return new StringBuilder("Bearer").append(" ").append(token).toString();
    }
}
