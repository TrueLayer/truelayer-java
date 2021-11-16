package truelayer.java;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.commons.lang3.reflect.FieldUtils.getField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@WireMockTest
public class IntegrationTests {

    TrueLayerClient tlClient;

    @SneakyThrows
    @BeforeEach
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo){
        tlClient = TrueLayerClient.builder()
                .clientCredentialsOptions(TestUtils.getClientCredentialsOptions())
                .build();

        writeField(tlClient, "endpointUrl", wireMockRuntimeInfo.getHttpBaseUrl(), true);
    }

    @Test
    public void shouldReturnAnUnauthorizedError(){
        stubFor(post("/connect/token").willReturn(unauthorized()));

        var thrown = Assertions.assertThrows(TrueLayerException.class,
                ()-> tlClient.auth().getOauthToken(List.of("paydirect")));

        assertNotNull(thrown);
    }

}
