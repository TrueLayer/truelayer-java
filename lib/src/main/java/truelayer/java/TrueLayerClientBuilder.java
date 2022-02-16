package truelayer.java;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static truelayer.java.Constants.Scopes.PAYMENTS;

import okhttp3.OkHttpClient;
import org.apache.commons.lang3.ObjectUtils;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.http.RetrofitFactory;
import truelayer.java.http.interceptors.AuthenticationInterceptor;
import truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import truelayer.java.http.interceptors.SignatureInterceptor;
import truelayer.java.http.interceptors.UserAgentInterceptor;
import truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import truelayer.java.payments.IPaymentsApi;
import truelayer.java.versioninfo.VersionInfo;
import truelayer.java.versioninfo.VersionInfoLoader;

/**
 * Builder class for TrueLayerClient instances.
 */
public class TrueLayerClientBuilder {
    private ClientCredentials clientCredentials;

    private SigningOptions signingOptions;

    // By default, production is used
    private Environment environment = Environment.live();

    TrueLayerClientBuilder() {}

    /**
     * Utility to set the client credentials required for Oauth2 protected endpoints.
     * @param credentials the credentials object that holds client id and secret.
     * @return the instance of the client builder used.
     * @see ClientCredentials
     */
    public TrueLayerClientBuilder clientCredentials(ClientCredentials credentials) {
        this.clientCredentials = credentials;
        return this;
    }

    /**
     * Utility to set the signing options required for payments.
     * @param signingOptions the signing options object that holds signature related informations.
     * @return the instance of the client builder used.
     * @see SigningOptions
     */
    public TrueLayerClientBuilder signingOptions(SigningOptions signingOptions) {
        this.signingOptions = signingOptions;
        return this;
    }

    /**
     * Utility to configure the library to interact a specific <i>TrueLayer</i> environment.
     * By default, <i>TrueLayer</i> production environment is used.
     * @param environment the environment to use
     * @return the instance of the client builder used.
     * @see Environment
     */
    public TrueLayerClientBuilder environment(Environment environment) {
        this.environment = environment;
        return this;
    }

    /**
     * Builds the Java library main class to interact with TrueLayer APIs.
     * @return a client instance
     * @see TrueLayerClient
     */
    public TrueLayerClient build() {
        if (ObjectUtils.isEmpty(clientCredentials)) {
            throw new TrueLayerException("client credentials must be set");
        }

        VersionInfo versionInfo = new VersionInfoLoader().load();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new IdempotencyKeyInterceptor());
        clientBuilder.addInterceptor(new UserAgentInterceptor(versionInfo));
        clientBuilder.addNetworkInterceptor(HttpLoggingInterceptor.New());
        OkHttpClient authHttpClient = clientBuilder.build();

        IAuthenticationHandler authenticationHandler = AuthenticationHandler.New()
                .clientCredentials(clientCredentials)
                .httpClient(RetrofitFactory.build(authHttpClient, environment.getAuthApiUri()))
                .build();

        IHostedPaymentPageLinkBuilder hppLinkBuilder =
                HostedPaymentPageLinkBuilder.New().uri(environment.getHppUri()).build();

        if (isEmpty(signingOptions)) {
            // return TL client with just authentication and HPP utils enabled
            return new TrueLayerClient(authenticationHandler, hppLinkBuilder);
        }

        // By using .newBuilder() we share internal OkHttpClient resources
        OkHttpClient paymentsHttpClient = authHttpClient
                .newBuilder()
                // we just need to add the signature and authentication interceptor
                // as all the others are inherited
                .addInterceptor(new SignatureInterceptor(signingOptions))
                .addInterceptor(new AuthenticationInterceptor(authenticationHandler, singletonList(PAYMENTS)))
                .build();
        IPaymentsApi paymentsHandler = RetrofitFactory.build(paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IPaymentsApi.class);

        return new TrueLayerClient(authenticationHandler, paymentsHandler, hppLinkBuilder);
    }
}
