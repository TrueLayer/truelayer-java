package truelayer.java;

import lombok.Builder;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.auth.IAuthenticationApi;
import truelayer.java.http.HttpClientFactory;
import truelayer.java.payments.IPaymentHandler;
import truelayer.java.payments.PaymentHandler;

@Builder
public class TrueLayerClient implements ITrueLayerClient{
    private ClientCredentialsOptions clientCredentialsOptions;
    private SigningOptions signingOptions;

    @Override
    public IAuthenticationHandler auth() {
        var authenticationApi = HttpClientFactory.getInstance()
                .create("https://auth.t7r.dev").create(IAuthenticationApi.class);

        return AuthenticationHandler.builder()
                .authenticationApi(authenticationApi)
                .clientCredentialsOptions(clientCredentialsOptions)
                .build();
    }

    @Override
    public IPaymentHandler payments() {
        return PaymentHandler.builder()
                .authenticationHandler(auth())
                .signingOptions(signingOptions)
                .build();
    }
}
