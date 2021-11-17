package truelayer.java;

import lombok.*;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationApi;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.HttpClientFactory;
import truelayer.java.payments.IPaymentHandler;
import truelayer.java.payments.PaymentHandler;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class TrueLayerClient implements ITrueLayerClient{
    private ClientCredentialsOptions clientCredentialsOptions;

    //todo wrap this into an optional and define a custom builder, not relying on lombok
    private SigningOptions signingOptions;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String endpointUrl;

    @SneakyThrows
    @Builder
    public TrueLayerClient(ClientCredentialsOptions clientCredentialsOptions,
                           SigningOptions signingOptions, boolean useSandbox){
        //todo useSandbox() to replace useSandbox(true|false)
        notNull(clientCredentialsOptions, "client credentials options must be set.");
        notEmpty(clientCredentialsOptions.getClientId(), "client id must be not empty");
        notEmpty(clientCredentialsOptions.getClientSecret(), "client secret must be not empty.");

        this.clientCredentialsOptions = clientCredentialsOptions;
        this.signingOptions = signingOptions;

        var properties = new Configurations().properties("application.properties");
        this.endpointUrl = properties.getString(String.format("tl.auth.endpoint.%s", useSandbox? "sandbox":"live"));
    }

    @Override
    public IAuthenticationHandler auth() {
        var authenticationApi = HttpClientFactory.getInstance()
                .create(getEndpointUrl()).create(IAuthenticationApi.class);

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
