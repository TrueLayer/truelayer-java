package truelayer.java.payments;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.signing.Signer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class Payments implements IPayments {

    //todo get from a config
    private static final String KID = "7695796e-e718-457d-845b-4a6be00ca454";
    private static final String DEV_PAYMENTS_URL = "https://test-pay-api.t7r.dev/payments";

    //todo replace with call to auth
    private static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVCM0ExQzhGODMyOTlEQjJCNTE3NUVGMDBGQjYwOTc2QTkwQTMzMjFSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6Ild6b2NqNE1wbmJLMUYxN3dEN1lKZHFrS015RSJ9.eyJuYmYiOjE2MzYwMjQ4MTcsImV4cCI6MTYzNjAyODQxNywiaXNzIjoiaHR0cHM6Ly9hdXRoLnQ3ci5kZXYiLCJhdWQiOlsicGF5ZGlyZWN0X2FwaSIsInBheW1lbnRzX2FwaSJdLCJjbGllbnRfaWQiOiJnaXVsaW9sZXNvLTg5OTNjOSIsImp0aSI6IkQ1MkQ3QTA2Q0VDNDg5MDg5OUMyNkJBRDBBNTVENzMzIiwiaWF0IjoxNjM2MDI0ODE3LCJzY29wZSI6WyJwYXlkaXJlY3QiLCJwYXltZW50cyJdfQ.n2H3NKtMqPP8rPhVnNzplVwwXMsn_C668IQ0HvzLPEhtjx4V5Ii_54FXQR12CRUJfKIA5n-7vKP0q9NoLiBkX2OeTQU_nQzshfaQhzcLLJcOOQkkaFObwr63nx3zzwNH9HbPNuw6BtGigZAmkw1W4tiiO6Q53duxhdkw1GqGH4b6jVszHtQp96yE6YB29LnAEzoNDxIeXH10_66u1Z0M4h16HR3ZxOtpsfxS46GgtJ1uNvc1OXuVWjS0S7MYDXkdaBKHoxwZKCKLoEnboO3O1yQeG2p435kePhDBEah6Km9SQv0WHtaqz0ywJ8cUyUf1hPP6zi_MyiBrzL59vt9ViA";

    private final RestTemplate restTemplate;

    public Payments() {
        this.restTemplate = new RestTemplate();;
    }

    @Override
    public Payment createPayment(CreatePaymentRequest createPaymentRequest) throws IOException {
        UUID idempotencyKey = generateIdempotencyKey();

        String createRequestJsonString = requestToJsonString(createPaymentRequest);
        byte[] createRequestJsonBytes = createRequestJsonString.getBytes(StandardCharsets.UTF_8);

        String signature = signRequest(idempotencyKey, createRequestJsonBytes, "/payments");

        HttpHeaders headers = getPaymentCreationHttpHeaders(idempotencyKey, signature);

        HttpEntity<String> httpRequest = new HttpEntity<>(createRequestJsonString, headers);

        //todo in Payments and Auth we are using 2 different type HttpClients - let's decide for 1 and use it everywhere
        ResponseEntity<Payment> exchange = restTemplate.exchange(DEV_PAYMENTS_URL, HttpMethod.POST, httpRequest, Payment.class);

        return exchange.getBody();
    }

    private HttpHeaders getPaymentCreationHttpHeaders(UUID idempotencyKey, String signature) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Idempotency-Key", idempotencyKey.toString());
        headers.add("Tl-Signature", signature);
        headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(ImmutableList.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    public Payment getPayment(String paymentId) {
        HttpEntity<String> entity = new HttpEntity<>(getPaymentRetrievalHttpHeaders());

        ResponseEntity<Payment> exchange = restTemplate.exchange(DEV_PAYMENTS_URL + "/" + paymentId, HttpMethod.GET,
                entity, Payment.class);

        return exchange.getBody();
    }

    @NotNull
    private HttpHeaders getPaymentRetrievalHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
        return headers;
    }


    private String signRequest(UUID idempotencyKey, byte[] jsonRequestBytes, String path) throws IOException {
        byte[] privateKey = Files.readAllBytes(Path.of("/Users/giulio.leso/Desktop/ec512-private-key.pem"));

        return Signer.from(KID, privateKey)
                .header("Idempotency-Key", idempotencyKey.toString())
                .method("post")
                .path(path)
                .body(jsonRequestBytes)
                .sign();
    }

    private String requestToJsonString(CreatePaymentRequest createPaymentRequest) {
        return new Gson().toJson(createPaymentRequest);
    }

    //todo we need to understand how to generate this in a meaningful way
    private UUID generateIdempotencyKey() {
        return UUID.randomUUID();
    }
}
