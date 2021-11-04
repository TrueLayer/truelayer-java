package truelayer.java.payments;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import truelayer.java.SigningOptions;
import truelayer.java.auth.IAuthentication;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.payments.exception.PaymentsException;
import truelayer.java.signing.Signer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Builder
public class Payments implements IPayments {

    //todo get from a config
    private static final List<String> SCOPES = ImmutableList.of("paydirect");
    private static final String KID = "7695796e-e718-457d-845b-4a6be00ca454";
    private static final String DEV_PAYMENTS_URL = "https://test-pay-api.t7r.dev/payments/";

    private RestTemplate restTemplate;
    private IAuthentication authentication;
    private String clientId;
    private String clientSecret;
    private SigningOptions signingOptions;

    @Override
    public Payment createPayment(CreatePaymentRequest createPaymentRequest) throws IOException, AuthenticationException, ParseException, JOSEException {
        UUID idempotencyKey = generateIdempotencyKey();

        String createRequestJsonString = requestToJsonString(createPaymentRequest);

        String signature = signRequest(idempotencyKey, createRequestJsonString, "/payments");

        HttpHeaders headers = getPaymentCreationHttpHeaders(idempotencyKey, signature, getAccessToken());

        HttpEntity<String> httpRequest = new HttpEntity<>(createRequestJsonString, headers);

        //todo in Payments and Auth we are using 2 different type HttpClients - let's decide for 1 and use it everywhere
        ResponseEntity<Payment> exchange = restTemplate.exchange(DEV_PAYMENTS_URL, HttpMethod.POST, httpRequest, Payment.class);

        //todo missing error handling
        return exchange.getBody();
    }

    public Payment getPayment(String paymentId) throws AuthenticationException {
        HttpEntity<String> entity = new HttpEntity<>(getPaymentRetrievalHttpHeaders(getAccessToken()));

        try {
            ResponseEntity<Payment> exchange = restTemplate.exchange(DEV_PAYMENTS_URL + paymentId, HttpMethod.GET,
                    entity, Payment.class);

            return exchange.getBody();
        } catch (Exception exception) {
            throw new PaymentsException(exception);
        }
    }

    @NotNull
    private HttpHeaders getPaymentRetrievalHttpHeaders(AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken.getAccessToken());
        return headers;
    }


    private String signRequest(UUID idempotencyKey, String jsonRequest, String path) throws IOException, ParseException, JOSEException {
        byte[] privateKey = signingOptions.getPrivateKey();

        return new Signer.Builder(KID, privateKey)
                .addHeader("Idempotency-Key", idempotencyKey.toString())
                .addHttpMethod("post")
                .addPath(path)
                .addBody(jsonRequest)
                .getSignature();
    }

    private String requestToJsonString(CreatePaymentRequest createPaymentRequest) {
        return new Gson().toJson(createPaymentRequest);
    }

    //todo we need to understand how to generate this in a meaningful way
    private UUID generateIdempotencyKey() {
        return UUID.randomUUID();
    }

    private AccessToken getAccessToken() throws AuthenticationException {
        return authentication.getOauthToken(SCOPES);
    }

    private HttpHeaders getPaymentCreationHttpHeaders(UUID idempotencyKey, String signature, AccessToken oauthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Idempotency-Key", idempotencyKey.toString());
        headers.add("Tl-Signature", signature);
        headers.add("Authorization", "Bearer " + oauthToken.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(ImmutableList.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
