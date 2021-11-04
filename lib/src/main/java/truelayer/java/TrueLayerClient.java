package truelayer.java;

import lombok.Builder;
import truelayer.java.auth.Authentication;
import truelayer.java.auth.IAuthentication;
import truelayer.java.payments.IPayments;
import truelayer.java.payments.Payments;

@Builder
public class TrueLayerClient implements ITrueLayerClient {

    private String clientId;

    private String clientSecret;

    private SigningOptions signingOptions;

    @Override
    public IAuthentication auth() {
        return Authentication.builder()
                .clientId(clientId)
                .clientSecret(clientSecret).build();
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
