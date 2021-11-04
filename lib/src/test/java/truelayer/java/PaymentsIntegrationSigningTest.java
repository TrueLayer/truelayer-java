package truelayer.java;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import truelayer.signing.Signer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentsIntegrationSigningTest {

    private static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVCM0ExQzhGODMyOTlEQjJCNTE3NUVGMDBGQjYwOTc2QTkwQTMzMjFSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6Ild6b2NqNE1wbmJLMUYxN3dEN1lKZHFrS015RSJ9.eyJuYmYiOjE2MzU5Njc0MjMsImV4cCI6MTYzNTk3MTAyMywiaXNzIjoiaHR0cHM6Ly9hdXRoLnQ3ci5kZXYiLCJhdWQiOlsicGF5ZGlyZWN0X2FwaSIsInBheW1lbnRzX2FwaSJdLCJjbGllbnRfaWQiOiJnaXVsaW9sZXNvLTg5OTNjOSIsImp0aSI6IkMxQUMzOEY5RDA2OUMxQ0IyMjAyQzQ0QjM3NDIxMDY2IiwiaWF0IjoxNjM1OTY3NDIzLCJzY29wZSI6WyJwYXlkaXJlY3QiLCJwYXltZW50cyJdfQ.lAkBP384vhAMqV9FZfVi7C2IotOhWg32wiOTyQbtqnBkwo9GkNyquk5gbwXy3jUROYuNRYyS3KSOJiR64qfsZFVHn880AiokfBAmO-0uv7wVL0a9jiN-uijVLrGiPTlJqd0UGKNMjmcEUERC6U1e11rAWawPpWcghtONrpI8zMIXXxBzrhgknaMNq1sNbff4GQ1z16RbeG1F8W6SxKXP1_abF1SF2v8wfBD-7FFmyt0xQi0yMqFBjih2GD2DQDo740XBQOi6lpdKWYZnyRBaV7CtITvFJCqsgmAdtSk5vHzlUaQAkjvpOc4zHX71sSBljHUCcAwLcMharAndveToIg";
    private static final String KID = "7695796e-e718-457d-845b-4a6be00ca454";


    //todo we need to pull all the config params out of this class
    @Test
    public void createAndRetrievePayment() throws IOException, ParseException {
        String idempotencyKey = "idemp-2076717c-9005-4811-a321-9e0787fa0382";
        byte[] privateKey = Files.readAllBytes(Path.of("/Users/giulio.leso/Desktop/ec512-private-key.pem"));

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
        byte[] bodyInBytes = body.getBytes(StandardCharsets.UTF_8);

        String signature = Signer.from(KID, privateKey)
                .header("Idempotency-Key", idempotencyKey)
                .method("post")
                .path(path)
                .body(bodyInBytes)
                .sign();

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
        JsonObject json = JsonParser.parseString(EntityUtils.toString(response.getEntity())).getAsJsonObject();
        String newPaymentId = json.get("id").getAsString();

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
