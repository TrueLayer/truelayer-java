package com.truelayer.java.http;

import com.truelayer.java.Utils;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.okhttp.v3_0.OkHttpTelemetry;
import java.net.URI;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitFactory {
    @Deprecated
    public static Retrofit build(OkHttpClient httpClient, URI baseUrl) {
        return new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(baseUrl.toString())
                .addConverterFactory(JacksonConverterFactory.create(Utils.getObjectMapper()))
                .addCallAdapterFactory(new TrueLayerApiAdapterFactory())
                .build();
    }

    public static Retrofit build(OkHttpClient httpClient, URI baseUrl, OpenTelemetry openTelemetry) {
        return new Retrofit.Builder()
                .callFactory(OkHttpTelemetry.builder(openTelemetry).build().newCallFactory(httpClient))
                .baseUrl(baseUrl.toString())
                .addConverterFactory(JacksonConverterFactory.create(Utils.getObjectMapper()))
                .addCallAdapterFactory(new TrueLayerApiAdapterFactory())
                .build();
    }
}
