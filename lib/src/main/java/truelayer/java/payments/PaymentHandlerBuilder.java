package truelayer.java.payments;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.List;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import truelayer.java.SigningOptions;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.configuration.Configuration;
import truelayer.java.http.HttpClientBuilder;
import truelayer.java.http.interceptors.AuthenticationInterceptor;
import truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import truelayer.java.http.interceptors.SignatureInterceptor;
import truelayer.java.http.interceptors.UserAgentInterceptor;

public class PaymentHandlerBuilder {
    private Configuration configuration;

    private SigningOptions signingOptions;

    private IAuthenticationHandler authenticationHandler;

    PaymentHandlerBuilder() {}

    public PaymentHandlerBuilder configuration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public PaymentHandlerBuilder signingOptions(SigningOptions signingOptions) {
        this.signingOptions = signingOptions;
        return this;
    }

    public PaymentHandlerBuilder authenticationHandler(IAuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
        return this;
    }

    public PaymentHandler build() {
        notNull(signingOptions, "signing options must be set");
        notEmpty(signingOptions.keyId(), "key id must be not empty");
        notNull(signingOptions.privateKey(), "private key must be not empty.");

        // todo replace with non deprecated or custom implementation
        var loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        var networkInterceptors = List.<Interceptor>of(loggingInterceptor);

        var applicationInterceptors = List.of(
                new IdempotencyKeyInterceptor(),
                new UserAgentInterceptor(configuration.versionInfo()),
                new SignatureInterceptor(signingOptions),
                new AuthenticationInterceptor(
                        authenticationHandler, configuration.payments().scopes()));

        var paymentHttpClient = new HttpClientBuilder()
                .baseUrl(configuration.payments().endpointUrl())
                .applicationInterceptors(applicationInterceptors)
                .networkInterceptors(networkInterceptors)
                .build();

        return new PaymentHandler(paymentHttpClient.create(IPaymentsApi.class));
    }
}
