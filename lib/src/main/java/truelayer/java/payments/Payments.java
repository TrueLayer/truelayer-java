package truelayer.java.payments;

import com.google.gson.Gson;
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
    private static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVCM0ExQzhGODMyOTlEQjJCNTE3NUVGMDBGQjYwOTc2QTkwQTMzMjFSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6Ild6b2NqNE1wbmJLMUYxN3dEN1lKZHFrS015RSJ9.eyJuYmYiOjE2MzYwMjA0MTAsImV4cCI6MTYzNjAyNDAxMCwiaXNzIjoiaHR0cHM6Ly9hdXRoLnQ3ci5kZXYiLCJhdWQiOlsicGF5ZGlyZWN0X2FwaSIsInBheW1lbnRzX2FwaSJdLCJjbGllbnRfaWQiOiJnaXVsaW9sZXNvLTg5OTNjOSIsImp0aSI6IkI0MUYwQ0Y2MEUxQTM3NjE2RDZFQjczNjQyMTBBNkE3IiwiaWF0IjoxNjM2MDIwNDEwLCJzY29wZSI6WyJwYXlkaXJlY3QiLCJwYXltZW50cyJdfQ.LCM4hOiwcT8sXYjdF-LjzEFwr-R2hOaRCUJxOxbP_AcxeHiClZXG7jsOrtjsrkhkSqIT8u0feKDBTY5NYJnKaALfvQC_sJRvCkTyzJDVqYE0HzrKIcmYoPsEVPytnceT0wTZ2PW2E49DDJ0aN8gDAtMPFdtjArD9FQR7mPBeSma35aZ9usR4ECSSINzv-j4wlSWHPVHB-e0EApfgvOYkHjKiSng6T_xOMeJ1TrPyf05Iygr4Yrre2dm5v7XnG1U4cxjnJ7k5e_ppbyQ0fyVEUjonxmjkQWH2c-e6j9NvwKCjJMgpFqB5vVzC4S5tJnJr8Ln2fvqGioIXfPmAfgha-A";

    @Override
    public Payment createPayment(CreatePaymentRequest createPaymentRequest) throws IOException {
        UUID idempotencyKey = generateIdempotencyKey();

        String createRequestJsonString = requestToJsonString(createPaymentRequest);
        byte[] createRequestJsonBytes = createRequestJsonString.getBytes(StandardCharsets.UTF_8);

        String signature = signRequest(idempotencyKey, createRequestJsonBytes, "/payments");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Idempotency-Key", idempotencyKey.toString());
        headers.add("Tl-Signature", signature);
        headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpRequest = new HttpEntity<>(createRequestJsonString, headers);

        //todo in Payments and Auth we are using 2 different type HttpClients - let's decide for 1 and use it everywhere
        ResponseEntity<Payment> exchange = restTemplate.exchange(DEV_PAYMENTS_URL, HttpMethod.POST, httpRequest, Payment.class);

        return exchange.getBody();
    }

    public Payment getPayment(String paymentId) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Payment> exchange = restTemplate.exchange(DEV_PAYMENTS_URL + "/" + paymentId, HttpMethod.GET,
                entity, Payment.class);

        return exchange.getBody();
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
