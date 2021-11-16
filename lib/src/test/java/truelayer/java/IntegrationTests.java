package truelayer.java;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WireMockTest(httpPort = 9000) //todo: try to randomize this
public class IntegrationTests {

    @Test
    public void shouldReturnAnUnauthorizedError(){
        stubFor(post("/connect/token").willReturn(unauthorized()));
        var tlClient = TrueLayerClient.builder()
                .clientCredentialsOptions(TestUtils.getClientCredentialsOptions())
                .build();

        var thrown = Assertions.assertThrows(TrueLayerException.class,
                ()-> tlClient.auth().getOauthToken(List.of("paydirect")));

        assertNotNull(thrown);
    }

}
