package truelayer.java.auth;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import truelayer.java.ClientCredentials;
import truelayer.java.configuration.Configuration;
import truelayer.java.http.HttpClientBuilder;
import truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import truelayer.java.http.interceptors.UserAgentInterceptor;

/**
 * Custom  builder class
 */
public class AuthenticationHandlerBuilder {

    private Configuration configuration;

    private ClientCredentials clientCredentials;

    private AuthenticationHandlerBuilder() {}

    public static AuthenticationHandlerBuilder New() {
        return new AuthenticationHandlerBuilder();
    }

    public AuthenticationHandlerBuilder configuration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    public AuthenticationHandlerBuilder clientCredentials(ClientCredentials clientCredentials) {
        this.clientCredentials = clientCredentials;
        return this;
    }

    public AuthenticationHandler build() {
        notNull(clientCredentials, "client credentials must be set");
        notEmpty(clientCredentials.clientId(), "client id must be not empty");
        notEmpty(clientCredentials.clientSecret(), "client secret must be not empty");

        // todo replace with non deprecated or custom implementation
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        List<Interceptor> networkInterceptors = Collections.singletonList(loggingInterceptor);

        List<Interceptor> applicationInterceptors =
                Arrays.asList(new IdempotencyKeyInterceptor(), new UserAgentInterceptor(configuration.versionInfo()));

        Retrofit authHttpClient = new HttpClientBuilder()
                .baseUrl(configuration.authentication().endpointUrl())
                .applicationInterceptors(applicationInterceptors)
                .networkInterceptors(networkInterceptors)
                .build();

        return new AuthenticationHandler(clientCredentials, authHttpClient.create(IAuthenticationApi.class));
    }
}
