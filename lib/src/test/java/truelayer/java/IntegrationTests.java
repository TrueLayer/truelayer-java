package truelayer.java;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import truelayer.java.payments.entities.CreatePaymentRequest;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@WireMockTest
@Tag("integration")
public class IntegrationTests {

    private TrueLayerClient tlClient;

    @SneakyThrows
    @BeforeEach
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        tlClient = TrueLayerClient.builder()
                .clientCredentialsOptions(TestUtils.getClientCredentialsOptions())
                .build();

        // don't try this at home
        writeField(tlClient, "endpointUrl", wireMockRuntimeInfo.getHttpBaseUrl(), true);
    }

    @Test
    @DisplayName("It should throw a TrueLayer exception in case on an authorized error from the auth API.")
    public void shouldThrowAnUnauthorizedError() {
        stubFor(
                post("/connect/token").willReturn(
                        badRequest()
                                .withBodyFile("auth/400.invalid_client.json")
                )
        );

        var thrown = Assertions.assertThrows(TrueLayerException.class,
                () -> tlClient.auth().getOauthToken(List.of("paydirect")));

        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains("\"error\": \"invalid_client\""));
    }

    @SneakyThrows
    @Test
    @DisplayName("It should yield an access token in case of 200")
    public void shouldReturnAnAccessToken() {
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );

        var accessToken = tlClient.auth().getOauthToken(List.of("paydirect"));

        assertNotNull(accessToken);
        assertTrue(!accessToken.getAccessToken().isEmpty());
        assertTrue(!accessToken.getTokenType().isEmpty());
        assertTrue(!accessToken.getScope().isEmpty());
        assertTrue(accessToken.getExpiresIn() > 0);
    }

    @Test
    @DisplayName("It should return a payment in case of 200")
    public void shouldReturnAPayment(){

        var payment = tlClient.payments().createPayment(CreatePaymentRequest.builder().build());

        assertNotNull(payment);
    }

}
