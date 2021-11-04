package truelayer.java;

import lombok.Builder;
import org.springframework.web.client.RestTemplate;
import truelayer.java.auth.Authentication;
import truelayer.java.auth.IAuthentication;
import truelayer.java.payments.IPayments;
import truelayer.java.payments.Payments;

import java.net.http.HttpClient;

@Builder
public class TrueLayerClient implements ITrueLayerClient{

    private String clientId;

    private String clientSecret;

    private SigningOptions signingOptions;

    @Override
    public IAuthentication Auth() {
        //todo extract
        TrueLayerHttpClient trueLayerHttpClient = TrueLayerHttpClient.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .httpClient(HttpClient.newHttpClient())
                .build();

        return Authentication.builder()
                .httpClient(trueLayerHttpClient)
                .build();
    }

    @Override
    public IPayments Payments() {
        return Payments.builder()
                .authentication(Auth())
                .clientId(clientId)
                .clientSecret(clientSecret)
                .restTemplate(new RestTemplate())
                .build();
    }

    /**
     * Optional configuration used for signed requests only.
     */
    @Builder
    static class SigningOptions {
        private String keyId;

        //todo: use a proper type
        private String privateKey;
    }
}
