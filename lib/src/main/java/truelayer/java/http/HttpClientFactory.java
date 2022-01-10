package truelayer.java.http;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import truelayer.java.http.adapters.ApiResponseCallAdapterFactory;
import truelayer.java.http.interceptors.UserAgentInterceptor;

public class HttpClientFactory implements IHttpClientFactory {

    private static HttpClientFactory INSTANCE = null;

    private HttpClientFactory(){}

    public static synchronized IHttpClientFactory getInstance() {
        if ( INSTANCE == null ) {
            INSTANCE = new HttpClientFactory();
        }
        return INSTANCE;
    }

    @Override
    public Retrofit create(String baseUrl) {
        var clientBuilder = new OkHttpClient.Builder();

        clientBuilder.addInterceptor(new UserAgentInterceptor());

        //todo replace with non deprecated or custom implementation
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(interceptor);

        return new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(new ApiResponseCallAdapterFactory())
                .build();
    }
}
