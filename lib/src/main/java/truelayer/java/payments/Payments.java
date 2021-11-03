package truelayer.java.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
    private static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVCM0ExQzhGODMyOTlEQjJCNTE3NUVGMDBGQjYwOTc2QTkwQTMzMjFSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6Ild6b2NqNE1wbmJLMUYxN3dEN1lKZHFrS015RSJ9.eyJuYmYiOjE2MzU5NzgxMDgsImV4cCI6MTYzNTk4MTcwOCwiaXNzIjoiaHR0cHM6Ly9hdXRoLnQ3ci5kZXYiLCJhdWQiOlsicGF5ZGlyZWN0X2FwaSIsInBheW1lbnRzX2FwaSJdLCJjbGllbnRfaWQiOiJnaXVsaW9sZXNvLTg5OTNjOSIsImp0aSI6IjExQzFFMjA5MkMwMkI4MUY0QjA1MjdFQjVGREJEQUZFIiwiaWF0IjoxNjM1OTc4MTA4LCJzY29wZSI6WyJwYXlkaXJlY3QiLCJwYXltZW50cyJdfQ.i2wCCiG8Fa4H0UK4sTyU1T90AREWRugdqewSj7Ncn1VmBCPYTOS8Shquxuuv0rbmWTMnogvGGpgW7N1Z0zFpgKsgr0Bnc_rbwah0XVvsm0xY5kjGUaNNz9Ae3o_iSJz5djbVjMtnqVs-rPzRHVfSFZw2KaYNszOWLDFGZ1sHPkFCCKjf16hPrw9zQarXBkGbeau5P45ddwEsHasmZQZAhuzc_MJYX3UzGGGkZn9byKbAprm7hKM4hu4huk2okv01FkpK0lNT6TlT_W93tjCGSA53xmuTB-yAZtnrOsYwi8GyNIZ4IjlR9O6goSusRV0G48jwLT9u0gfmf-ixnW5ipg";

    @Override
    public Payment createPayment(CreatePaymentRequest request) throws IOException {
        UUID idempotencyKey = generateIdempotencyKey();

        String signature = signRequest(idempotencyKey, request, "/payments");


        //-------there is a problem between the JSON used as a body in the call and the one used in the signature
        //just a test
        String body = "{\n" +
                "\"amount_in_minor\": 177,\n" +
                "\"currency\": \"GBP\",\n" +
                "\"payment_method\": {\n" +
                "\"type\": \"bank_transfer\"\n" +
                "},\n" +
                "\"beneficiary\": {\n" +
                "\"type\": \"merchant_account\",\n" +
                "\"id\": \"c54104a5-fdd1-4277-8793-dbfa511c898b\",\n" +
                "\"name\": \"Some merchant\"\n" +
                "},\n" +
                "\"user\": {\n" +
                "\"type\": \"new\",\n" +
                "\"name\": \"Wajid Malik\",\n" +
                "\"email\": \"wajid.malik@truelayer.com\"\n" +
                "}\n" +
                "}\n";
        //delete


        HttpHeaders headers = new HttpHeaders();
        headers.add("Idempotency-Key", idempotencyKey.toString());
        headers.add("Tl-Signature", signature);
        headers.add("Authorization", "Bearer " + ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpRequest = new HttpEntity<>(body, headers);
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


    private String signRequest(UUID idempotencyKey, CreatePaymentRequest request, String path) throws IOException {
        byte[] privateKey = Files.readAllBytes(Path.of("/Users/giulio.leso/Desktop/ec512-private-key.pem"));

//        String jsonRequest = requestToJsonString(request);
//        byte[] jsonRequestBytes = jsonRequest.getBytes(StandardCharsets.UTF_8);

        String body = "{\n" +
                "\"amount_in_minor\": 177,\n" +
                "\"currency\": \"GBP\",\n" +
                "\"payment_method\": {\n" +
                "\"type\": \"bank_transfer\"\n" +
                "},\n" +
                "\"beneficiary\": {\n" +
                "\"type\": \"merchant_account\",\n" +
                "\"id\": \"c54104a5-fdd1-4277-8793-dbfa511c898b\",\n" +
                "\"name\": \"Some merchant\"\n" +
                "},\n" +
                "\"user\": {\n" +
                "\"type\": \"new\",\n" +
                "\"name\": \"Wajid Malik\",\n" +
                "\"email\": \"wajid.malik@truelayer.com\"\n" +
                "}\n" +
                "}\n";
        byte[] bodyInBytes = body.getBytes(StandardCharsets.UTF_8);

        return Signer.from(KID, privateKey)
                .header("Idempotency-Key", idempotencyKey.toString())
                .method("post")
                .path(path)
                .body(bodyInBytes)
                .sign();
    }

    private String requestToJsonString(CreatePaymentRequest request) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }

    //todo we need to understand how to generate this in a meaningful way
    private UUID generateIdempotencyKey() {
        return UUID.randomUUID();
    }
}
