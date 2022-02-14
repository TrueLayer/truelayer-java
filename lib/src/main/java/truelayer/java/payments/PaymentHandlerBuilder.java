package truelayer.java.payments;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static truelayer.java.common.Constants.Scopes.PAYMENTS;

import java.util.Arrays;
import java.util.List;
import okhttp3.Interceptor;
import retrofit2.Retrofit;
import truelayer.java.Environment;
import truelayer.java.SigningOptions;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.HttpClientBuilder;
import truelayer.java.http.interceptors.*;
import truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import truelayer.java.versioninfo.VersionInfo;

public class PaymentHandlerBuilder {
    private VersionInfo versionInfo;

    private Environment environment;

    private SigningOptions signingOptions;

    private IAuthenticationHandler authenticationHandler;

    PaymentHandlerBuilder() {}

    public PaymentHandlerBuilder environment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public PaymentHandlerBuilder versionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
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

        List<Interceptor> networkInterceptors = singletonList(HttpLoggingInterceptor.New());

        List<Interceptor> applicationInterceptors = Arrays.asList(
                new IdempotencyKeyInterceptor(),
                new UserAgentInterceptor(versionInfo),
                new SignatureInterceptor(signingOptions),
                new AuthenticationInterceptor(authenticationHandler, singletonList(PAYMENTS)));

        Retrofit paymentHttpClient = new HttpClientBuilder()
                .baseUrl(environment.getPaymentsApiUri().toString())
                .applicationInterceptors(applicationInterceptors)
                .networkInterceptors(networkInterceptors)
                .build();

        return new PaymentHandler(paymentHttpClient.create(IPaymentsApi.class));
    }
}
