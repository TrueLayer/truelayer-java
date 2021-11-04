package truelayer.java.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.nimbusds.jose.JOSEException;
import lombok.Builder;
import org.apache.http.entity.ContentType;
import truelayer.java.SigningOptions;
import truelayer.java.auth.IAuthentication;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.payments.exception.PaymentsException;
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
public class Payments implements IPayments {

    //todo get from a config
    private static final List<String> SCOPES = ImmutableList.of("paydirect");
    private static final String DEV_PAYMENTS_URL = "https://test-pay-api.t7r.dev/payments";

    private IAuthentication authentication;
    private String clientId;
    private String clientSecret;
    private SigningOptions signingOptions;

    @Override
    public Payment createPayment(CreatePaymentRequest createPaymentRequest) throws IOException, AuthenticationException, ParseException, JOSEException {
        UUID idempotencyKey = generateIdempotencyKey();

        String createRequestJsonString = requestToJsonString(createPaymentRequest);

        String signature = signRequest(idempotencyKey, createRequestJsonString, "/payments");

        var httpRequestBuilder = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(DEV_PAYMENTS_URL))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(createRequestJsonString));
        var headers = getPaymentCreationHttpHeaders(idempotencyKey, signature, getAccessToken());
        headers.forEach(httpRequestBuilder::header);

        var httpClient = HttpClient.newHttpClient();
        try {
            var response = httpClient.send(httpRequestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() >= 400)
                throw new PaymentsException(String.valueOf(response.statusCode()), response.body());

            return new ObjectMapper().readValue(response.body(), Payment.class);
        } catch (Exception e) {
            throw new PaymentsException(e);
        }
    }

    public Payment getPayment(String paymentId) throws AuthenticationException {
        var httpRequest = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(DEV_PAYMENTS_URL + "/" + paymentId))
                .header("Authorization", "Bearer " + getAccessToken())
                .GET()
                .build();
        var httpClient = HttpClient.newHttpClient();
        try {
            var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() >= 400)
                throw new PaymentsException(String.valueOf(response.statusCode()), response.body());

            return new ObjectMapper().readValue(response.body(), Payment.class);
        } catch (Exception e) {
            throw new PaymentsException(e);
        }
    }


    private String signRequest(UUID idempotencyKey, String jsonRequest, String path) throws IOException, ParseException, JOSEException {
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

    private String getAccessToken() throws AuthenticationException {
        return authentication.getOauthToken(SCOPES).getAccessToken();
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
