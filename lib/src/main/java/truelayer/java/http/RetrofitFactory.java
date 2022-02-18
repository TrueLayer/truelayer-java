package truelayer.java.http;

import static truelayer.java.Utils.getObjectMapper;

import java.net.URI;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitFactory {
    public static Retrofit build(OkHttpClient httpClient, URI baseUrl) {
        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(baseUrl.toString())
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
                .addCallAdapterFactory(new TrueLayerApiAdapterFactory())
                .build();
    }
}
