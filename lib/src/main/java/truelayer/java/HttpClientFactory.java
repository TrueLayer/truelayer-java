package truelayer.java;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
        //todo replace with non deprecated implementation
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
