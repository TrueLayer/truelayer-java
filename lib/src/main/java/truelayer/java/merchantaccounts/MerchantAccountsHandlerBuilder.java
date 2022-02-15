package truelayer.java.merchantaccounts;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.Validate.notNull;
import static truelayer.java.Constants.Scopes.PAYMENTS;

import java.util.Arrays;
import java.util.List;
import okhttp3.Interceptor;
import retrofit2.Retrofit;
import truelayer.java.Environment;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.HttpClientBuilder;
import truelayer.java.http.interceptors.AuthenticationInterceptor;
import truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import truelayer.java.http.interceptors.UserAgentInterceptor;
import truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import truelayer.java.versioninfo.VersionInfo;

public class MerchantAccountsHandlerBuilder {
    private VersionInfo versionInfo;

    private Environment environment;

    private IAuthenticationHandler authenticationHandler;

    MerchantAccountsHandlerBuilder() {}

    public MerchantAccountsHandlerBuilder environment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public MerchantAccountsHandlerBuilder versionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
        return this;
    }

    public MerchantAccountsHandlerBuilder authenticationHandler(IAuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
        return this;
    }

    public MerchantAccountsHandler build() {
        // todo: reuse the existing payments API client ?
        notNull(versionInfo, "version info file not present");

        notNull(environment, "environment must be set");

        List<Interceptor> networkInterceptors = singletonList(HttpLoggingInterceptor.New());

        List<Interceptor> applicationInterceptors = Arrays.asList(
                new IdempotencyKeyInterceptor(),
                new UserAgentInterceptor(versionInfo),
                new AuthenticationInterceptor(authenticationHandler, singletonList(PAYMENTS)));

        // todo: reuse the existing payments API client ?
        Retrofit merchantAccountsHttpClient = new HttpClientBuilder()
                .baseUrl(environment.getPaymentsApiUri().toString())
                .applicationInterceptors(applicationInterceptors)
                .networkInterceptors(networkInterceptors)
                .build();

        return new MerchantAccountsHandler(merchantAccountsHttpClient.create(IMerchantAccountsApi.class));
    }
}
