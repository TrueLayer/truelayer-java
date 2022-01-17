package truelayer.java.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import truelayer.java.TrueLayerException;
import truelayer.java.http.adapters.ApiResponseCallAdapterFactory;

import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

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

        var objectMapper = new ObjectMapper();

        // required for optionals deserialization
        objectMapper.registerModule(new Jdk8Module());

        return new Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(new ApiResponseCallAdapterFactory())
                .build();
    }
}
