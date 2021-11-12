package truelayer.java;

import lombok.Builder;
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
    public IAuthentication auth() {
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
    public IPayments payments() {
        return Payments.builder()
                .authentication(auth())
                .clientId(clientId)
                .clientSecret(clientSecret)
                .signingOptions(signingOptions)
                .build();
    }
}
