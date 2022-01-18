package truelayer.java.http;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static truelayer.java.Utils.getObjectMapper;

import java.util.List;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import truelayer.java.TrueLayerException;
import truelayer.java.http.adapters.ApiResponseCallAdapterFactory;

public class HttpClientBuilder {

    private String baseUrl;

    private List<Interceptor> applicationInterceptors;
    private List<Interceptor> networkInterceptors;

    public HttpClientBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public HttpClientBuilder applicationInterceptors(List<Interceptor> applicationInterceptors) {
        this.applicationInterceptors = applicationInterceptors;
        return this;
    }

    public HttpClientBuilder networkInterceptors(List<Interceptor> networkInterceptors) {
        this.networkInterceptors = networkInterceptors;
        return this;
    }

    public Retrofit build() {
        if (isEmpty(baseUrl)) {
            throw new TrueLayerException("base url must be specified");
        }

        var clientBuilder = new OkHttpClient.Builder();

        if (isNotEmpty(applicationInterceptors)) {
            applicationInterceptors.forEach(clientBuilder::addInterceptor);
        }

        if (isNotEmpty(networkInterceptors)) {
            networkInterceptors.forEach(clientBuilder::addInterceptor);
        }

        return new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
                .addCallAdapterFactory(new ApiResponseCallAdapterFactory())
                .build();
    }
}
