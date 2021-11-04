package truelayer.java;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.util.JSONObjectUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import truelayer.java.signing.Signer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentsIntegrationSigningTest {

    private static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVCM0ExQzhGODMyOTlEQjJCNTE3NUVGMDBGQjYwOTc2QTkwQTMzMjFSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6Ild6b2NqNE1wbmJLMUYxN3dEN1lKZHFrS015RSJ9.eyJuYmYiOjE2MzU5NzgxMDgsImV4cCI6MTYzNTk4MTcwOCwiaXNzIjoiaHR0cHM6Ly9hdXRoLnQ3ci5kZXYiLCJhdWQiOlsicGF5ZGlyZWN0X2FwaSIsInBheW1lbnRzX2FwaSJdLCJjbGllbnRfaWQiOiJnaXVsaW9sZXNvLTg5OTNjOSIsImp0aSI6IjExQzFFMjA5MkMwMkI4MUY0QjA1MjdFQjVGREJEQUZFIiwiaWF0IjoxNjM1OTc4MTA4LCJzY29wZSI6WyJwYXlkaXJlY3QiLCJwYXltZW50cyJdfQ.i2wCCiG8Fa4H0UK4sTyU1T90AREWRugdqewSj7Ncn1VmBCPYTOS8Shquxuuv0rbmWTMnogvGGpgW7N1Z0zFpgKsgr0Bnc_rbwah0XVvsm0xY5kjGUaNNz9Ae3o_iSJz5djbVjMtnqVs-rPzRHVfSFZw2KaYNszOWLDFGZ1sHPkFCCKjf16hPrw9zQarXBkGbeau5P45ddwEsHasmZQZAhuzc_MJYX3UzGGGkZn9byKbAprm7hKM4hu4huk2okv01FkpK0lNT6TlT_W93tjCGSA53xmuTB-yAZtnrOsYwi8GyNIZ4IjlR9O6goSusRV0G48jwLT9u0gfmf-ixnW5ipg";
    private static final String KID = "7695796e-e718-457d-845b-4a6be00ca454";


    //todo we need to pull all the config params out of this class
    @Test
    public void createAndRetrievePayment() throws IOException, ParseException, JOSEException {
        String idempotencyKey = "idemp-2076717c-9005-4811-a321-9e0787fa038d";
        byte[] privateKey = Files.readAllBytes(Path.of("src/test/resources/ec512-private-key.pem"));

        String path = "/payments";
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

        var signature = new Signer.Builder(KID, privateKey)
                .addHeader("Idempotency-Key", idempotencyKey.toString())
                .addHttpMethod("post")
                .addPath(path)
                .addBody(body)
                .getSignature();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://test-pay-api.t7r.dev/payments");

        httpPost.addHeader("Idempotency-Key", idempotencyKey);
        httpPost.addHeader("Tl-Signature", signature);
        httpPost.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);

        httpPost.setEntity(new StringEntity(body));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        //assert the payment creation call is successful
        CloseableHttpResponse response = client.execute(httpPost);
        assertEquals(response.getStatusLine().getStatusCode(), 201);

        //retrieve the payment id
        Map<String, Object> jsonMapResponse = JSONObjectUtils.parse(EntityUtils.toString(response.getEntity()));
        String newPaymentId = (String) jsonMapResponse.get("id");

        //get the newly created payment using the id
        HttpGet httpGet = new HttpGet("https://test-pay-api.t7r.dev/payments/" + newPaymentId);
        httpGet.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);

        CloseableHttpResponse getResponse = client.execute(httpGet);

        //assert the get call is successful
        assertEquals(getResponse.getStatusLine().getStatusCode(), 200);
        client.close();
    }

    @Test
    public void noPaymentsPresent() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://test-pay-api.t7r.dev/payments/1");
        httpGet.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);

        CloseableHttpResponse response = client.execute(httpGet);

        assertEquals(response.getStatusLine().getStatusCode(), 404);
        client.close();
    }
}
