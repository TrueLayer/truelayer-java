package com.truelayer.java.http;

import com.truelayer.java.Utils;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.instrumentation.okhttp.v3_0.OkHttpTelemetry;
import java.net.URI;

import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static io.opentelemetry.semconv.ResourceAttributes.SERVICE_NAME;
import static io.opentelemetry.semconv.ResourceAttributes.SERVICE_VERSION;

public class RetrofitFactory {
    public static Retrofit build(OkHttpClient httpClient, URI baseUrl) {
        Resource resource = Resource.getDefault().toBuilder().put(SERVICE_NAME, "truelayer-java")
                .put(SERVICE_VERSION, "0.1.0").build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(OtlpGrpcSpanExporter.builder().build()))
                .setResource(resource)
                .build();

//        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder()
//                .registerMetricReader(PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder().build()).build())
//                .setResource(resource)
//                .build();

        SdkLoggerProvider sdkLoggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(
                        BatchLogRecordProcessor.builder(OtlpGrpcLogRecordExporter.builder().build()).build())
                .setResource(resource)
                .build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                //.setMeterProvider(sdkMeterProvider)
                .setLoggerProvider(sdkLoggerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .build();
                //.buildAndRegisterGlobal();

        return new Retrofit.Builder()
                //.client(httpClient)
                .callFactory(OkHttpTelemetry.builder(openTelemetry).build().newCallFactory(httpClient))
                .baseUrl(baseUrl.toString())
                .addConverterFactory(JacksonConverterFactory.create(Utils.getObjectMapper()))
                .addCallAdapterFactory(new TrueLayerApiAdapterFactory())
                .build();
    }
}
