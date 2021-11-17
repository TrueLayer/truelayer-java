package truelayer.java;

import lombok.*;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationApi;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.HttpClientFactory;
import truelayer.java.payments.IPaymentHandler;
import truelayer.java.payments.IPaymentsApi;
import truelayer.java.payments.PaymentHandler;

import java.util.List;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class TrueLayerClient implements ITrueLayerClient{
    private ClientCredentialsOptions clientCredentialsOptions;

    //todo wrap this into an optional and define a custom builder, not relying on lombok
    private SigningOptions signingOptions;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String authEndpointUrl;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String paymentsEndpointUrl;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private String[] paymentsScopes;

    @SneakyThrows
    @Builder
    public TrueLayerClient(ClientCredentialsOptions clientCredentialsOptions,
                           SigningOptions signingOptions, boolean useSandbox){
        this.clientCredentialsOptions = clientCredentialsOptions;
        this.signingOptions = signingOptions;

        var properties = new Configurations().properties("application.properties");
        this.authEndpointUrl = properties.getString(String.format("tl.auth.endpoint.%s", useSandbox? "sandbox":"live"));
        this.paymentsEndpointUrl = properties.getString(String.format("tl.payments.endpoint.%s", useSandbox? "sandbox":"live"));
        this.paymentsScopes = properties.getStringArray("tl.payments.scopes");
    }

    @Override
    @SneakyThrows
    public IAuthenticationHandler auth() {
        //todo: avoid building every time auth() is called
        notNull(clientCredentialsOptions, "client credentials options must be set.");
        notEmpty(clientCredentialsOptions.getClientId(), "client id must be not empty");
        notEmpty(clientCredentialsOptions.getClientSecret(), "client secret must be not empty.");

        var authenticationApi = HttpClientFactory.getInstance()
                .create(getAuthEndpointUrl()).create(IAuthenticationApi.class);

        return AuthenticationHandler.builder()
                .authenticationApi(authenticationApi)
                .clientCredentialsOptions(clientCredentialsOptions)
                .build();
    }

    @Override
    public IPaymentHandler payments() {
        var paymentHandlerBuilder = PaymentHandler.builder()
                .authenticationHandler(auth());

        //todo: avoid building every time payments() is called
        notNull(signingOptions, "signing options must be set.");
        notEmpty(signingOptions.getKeyId(), "key id must be not empty");
        notNull(signingOptions.getPrivateKey(), "private key must be set.");

        var paymentsApi = HttpClientFactory.getInstance()
                .create(getPaymentsEndpointUrl()).create(IPaymentsApi.class);
        return paymentHandlerBuilder.paymentsApi(paymentsApi)
                .paymentsScopes(paymentsScopes)
                .signingOptions(signingOptions)
                .build();
    }
}
