package truelayer.java.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.http.entity.ContentType;
import truelayer.java.SigningOptions;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.TrueLayerException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.payments.exception.PaymentException;
import truelayer.java.signing.Signer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
public class PaymentHandler implements IPaymentHandler {

    private IAuthenticationHandler authenticationHandler;
    private IPaymentsApi paymentsApi;
    private String[] paymentsScopes;

    private SigningOptions signingOptions;

    @SneakyThrows
    @Override
    public Payment createPayment(CreatePaymentRequest createPaymentRequest) {
        UUID idempotencyKey = generateIdempotencyKey();

        String createRequestJsonString = requestToJsonString(createPaymentRequest);

        String signature = signRequest(idempotencyKey, createRequestJsonString, "/payments");

        var httpRequestBuilder = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create("DEV_PAYMENTS_URL"))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(createRequestJsonString));
        var headers = getPaymentCreationHttpHeaders(idempotencyKey, signature, getAccessToken());
        headers.forEach(httpRequestBuilder::header);

        var httpClient = HttpClient.newHttpClient();
        try {
            var response = httpClient.send(httpRequestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() >= 400)
                throw new PaymentException(String.valueOf(response.statusCode()), response.body());

            return new ObjectMapper().readValue(response.body(), Payment.class);
        } catch (Exception e) {
            throw new PaymentException(e);
        }
    }

    public Payment getPayment(String paymentId) throws TrueLayerException, IOException {
        var httpRequest = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create("DEV_PAYMENTS_URL" + "/" + paymentId))
                .header("Authorization", "Bearer " + getAccessToken())
                .GET()
                .build();
        var httpClient = HttpClient.newHttpClient();
        try {
            var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() >= 400)
                throw new PaymentException(String.valueOf(response.statusCode()), response.body());

            return new ObjectMapper().readValue(response.body(), Payment.class);
        } catch (Exception e) {
            throw new PaymentException(e);
        }
    }


    private String signRequest(UUID idempotencyKey, String jsonRequest, String path) throws ParseException, JOSEException {
        byte[] privateKey = signingOptions.getPrivateKey();

        return new Signer.Builder(signingOptions.getKeyId(), privateKey)
                .addHeader("Idempotency-Key", idempotencyKey.toString())
                .addHttpMethod("post")
                .addPath(path)
                .addBody(jsonRequest)
                .getSignature();
    }

    private String requestToJsonString(CreatePaymentRequest createPaymentRequest) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(createPaymentRequest);
    }

    //todo we need to understand how to generate this in a meaningful way
    private UUID generateIdempotencyKey() {
        return UUID.randomUUID();
    }

    private String getAccessToken() throws TrueLayerException, IOException {
        return authenticationHandler.getOauthToken(List.of("SCOPES")).getAccessToken();
    }

    private Map<String, String> getPaymentCreationHttpHeaders(UUID idempotencyKey, String signature, String accessToken) {
        return Map.of(
                "Idempotency-Key", idempotencyKey.toString(),
                "Tl-Signature", signature,
                "Authorization", "Bearer " + accessToken,
                org.apache.http.HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString(),
                org.apache.http.HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString()
        );
    }
}
