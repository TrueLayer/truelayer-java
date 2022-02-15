package truelayer.java.http;

import static java.util.Collections.singletonList;
import static truelayer.java.common.Constants.Scopes.PAYMENTS;
import static truelayer.java.common.Utils.getObjectMapper;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import truelayer.java.Environment;
import truelayer.java.SigningOptions;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.interceptors.AuthenticationInterceptor;
import truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import truelayer.java.http.interceptors.SignatureInterceptor;
import truelayer.java.http.interceptors.UserAgentInterceptor;
import truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import truelayer.java.versioninfo.VersionInfo;

@RequiredArgsConstructor
public class HttpClientFactory {

    private final Environment environment;

    private final VersionInfo versionInfo;

    private final SigningOptions signingOptions;

    public Retrofit newAuthApiHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.addInterceptor(new IdempotencyKeyInterceptor());
        clientBuilder.addInterceptor(new UserAgentInterceptor(versionInfo));

        clientBuilder.addNetworkInterceptor(HttpLoggingInterceptor.New());

        return new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(environment.getAuthApiUri().toString())
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
                .addCallAdapterFactory(new TrueLayerApiAdapterFactory())
                .build();
    }

    public Retrofit newPaymentsApiHttpClient(IAuthenticationHandler authenticationHandler) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.addInterceptor(new IdempotencyKeyInterceptor());
        clientBuilder.addInterceptor(new UserAgentInterceptor(versionInfo));
        clientBuilder.addInterceptor(new SignatureInterceptor(signingOptions));
        clientBuilder.addInterceptor(new AuthenticationInterceptor(authenticationHandler, singletonList(PAYMENTS)));

        clientBuilder.addNetworkInterceptor(HttpLoggingInterceptor.New());

        return new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(environment.getPaymentsApiUri().toString())
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
                .addCallAdapterFactory(new TrueLayerApiAdapterFactory())
                .build();
    }
}
