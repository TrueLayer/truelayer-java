package truelayer.java;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TrueLayerClientTests {

    @Test
    @DisplayName("It should yield an authentication handler")
    @SneakyThrows
    public void itShouldBuildAnAuthenticationHandler() {
       var trueLayerClient = TrueLayerClient.builder()
                .clientCredentialsOptions(TestUtils.getClientCredentialsOptions())
                .build();

        var auth = trueLayerClient.auth();

        Assertions.assertNotNull(auth);
    }

    @Disabled
    @Test
    @DisplayName("It should yield a payment handler")
    public void itShouldBuildAPaymentClient() {
        //todo
    }
}
