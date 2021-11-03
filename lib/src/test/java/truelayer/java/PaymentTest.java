package truelayer.java;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import truelayer.signing.Signer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class PaymentTest {

    static String KID = "7695796e-e718-457d-845b-4a6be00ca454";

    @Test
    public void createTestPayment() throws IOException {
        String idempotencyKey = "idemp-2076717c-9005-4811-a321-9e0787fa0382";
        byte[] privateKey = Files.readAllBytes(Path.of("/Users/giulio.leso/Desktop/ec512-private-key.pem"));
        String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVCM0ExQzhGODMyOTlEQjJCNTE3NUVGMDBGQjYwOTc2QTkwQTMzMjFSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6Ild6b2NqNE1wbmJLMUYxN3dEN1lKZHFrS015RSJ9.eyJuYmYiOjE2MzU5NTU5OTMsImV4cCI6MTYzNTk1OTU5MywiaXNzIjoiaHR0cHM6Ly9hdXRoLnQ3ci5kZXYiLCJhdWQiOlsicGF5ZGlyZWN0X2FwaSIsInBheW1lbnRzX2FwaSJdLCJjbGllbnRfaWQiOiJnaXVsaW9sZXNvLTg5OTNjOSIsImp0aSI6Ijc0QkY0QkZGRjAyNTQ0NzRBOTQyQTgxMDY2MTc1ODFCIiwiaWF0IjoxNjM1OTU1OTkzLCJzY29wZSI6WyJwYXlkaXJlY3QiLCJwYXltZW50cyJdfQ.GnIJrofKlOh1zFNdn9CEi1IlRnXbYdo-16VeTXUuMVria3OWl1x5Uux8IZV2gotG_Yf6KUOLwfBez2BToy0ZT4mso3LawqZ8SmVV50m-KuwY_8F1QMmkSnrjZXucqIIv72b9rFOaUw4ScupG8fqq99goB_WuvauL1H3-XQYm5fceSSQ5LSUGaK_ssDTsV_gP3DzQ81kFIRmK9lXIRQW0SiBsUIAJ_pqR7A1qrITosokzDmwtwf8AapT90D7fI-9FzMmayXuNfb_1Y5Rh56TQV98J3AhyScWLgYq0DJa7yPm5XGQ1Ao7IeYHZm29MS2wexnZ_J2HKvQw9MuVHXRympQ";

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
        httpPost.addHeader("Authorization", "Bearer " + accessToken);

        httpPost.setEntity(new StringEntity(body));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = client.execute(httpPost);

        System.out.println("request" + httpPost);
        for (Header header : httpPost.getAllHeaders()) {
            System.out.println(header.getName() + ": " + header.getValue());
        }
        
        System.out.println("response" + EntityUtils.toString(response.getEntity(), "UTF-8"));
        System.out.println("TL signature: " + signature);

        assertEquals(response.getStatusLine().getStatusCode(), 201);
        client.close();
    }

    @Test
    public void getPayment() throws IOException {
        String accessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjVCM0ExQzhGODMyOTlEQjJCNTE3NUVGMDBGQjYwOTc2QTkwQTMzMjFSUzI1NiIsInR5cCI6ImF0K2p3dCIsIng1dCI6Ild6b2NqNE1wbmJLMUYxN3dEN1lKZHFrS015RSJ9.eyJuYmYiOjE2MzU5NTU5OTMsImV4cCI6MTYzNTk1OTU5MywiaXNzIjoiaHR0cHM6Ly9hdXRoLnQ3ci5kZXYiLCJhdWQiOlsicGF5ZGlyZWN0X2FwaSIsInBheW1lbnRzX2FwaSJdLCJjbGllbnRfaWQiOiJnaXVsaW9sZXNvLTg5OTNjOSIsImp0aSI6Ijc0QkY0QkZGRjAyNTQ0NzRBOTQyQTgxMDY2MTc1ODFCIiwiaWF0IjoxNjM1OTU1OTkzLCJzY29wZSI6WyJwYXlkaXJlY3QiLCJwYXltZW50cyJdfQ.GnIJrofKlOh1zFNdn9CEi1IlRnXbYdo-16VeTXUuMVria3OWl1x5Uux8IZV2gotG_Yf6KUOLwfBez2BToy0ZT4mso3LawqZ8SmVV50m-KuwY_8F1QMmkSnrjZXucqIIv72b9rFOaUw4ScupG8fqq99goB_WuvauL1H3-XQYm5fceSSQ5LSUGaK_ssDTsV_gP3DzQ81kFIRmK9lXIRQW0SiBsUIAJ_pqR7A1qrITosokzDmwtwf8AapT90D7fI-9FzMmayXuNfb_1Y5Rh56TQV98J3AhyScWLgYq0DJa7yPm5XGQ1Ao7IeYHZm29MS2wexnZ_J2HKvQw9MuVHXRympQ";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://test-pay-api.t7r.dev/payments/7408ee47-e208-419b-ab30-3515108f5755");
        httpGet.addHeader("Authorization", "Bearer " + accessToken);


        CloseableHttpResponse response = client.execute(httpGet);

        System.out.println("request" + httpGet);
        for (Header header : httpGet.getAllHeaders()) {
            System.out.println(header.getName() + ": " + header.getValue());
        }

        System.out.println("response" + response);

        assertEquals(response.getStatusLine().getStatusCode(), 200);
        client.close();
    }
}
