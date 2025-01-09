package com.truelayer.quarkusmvc;

import com.truelayer.java.*;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.opentelemetry.semconv.ServiceAttributes.SERVICE_NAME;
import static io.opentelemetry.semconv.ServiceAttributes.SERVICE_VERSION;

@ApplicationScoped
public class TrueLayerClientProvider {

    @ConfigProperty(name = "tl.client.id")
    String clientId;

    @ConfigProperty(name = "tl.client.secret")
    String clientSecret;

    @ConfigProperty(name = "tl.signing.key_id")
    String signingKeyId;

    @ConfigProperty(name = "tl.signing.private_key_location")
    String signingPrivateKeyLocation;

    private static final Logger LOG = Logger.getLogger(TrueLayerClientProvider.class);

    @ApplicationScoped
    @SneakyThrows
    public ITrueLayerClient producer() throws IOException {
        return TrueLayerClient.New()
                .withOpenTelemetry(buildOpenTelemetryConfig())
                .environment(Environment.sandbox())
                .clientCredentials(
                        ClientCredentials.builder()
                                .clientId(clientId)
                                .clientSecret(clientSecret)
                            .build()
                )
                /*.signingOptions(SigningOptions.builder()
                        .keyId(signingKeyId)
                        .privateKey(Files.readAllBytes(Path.of(signingPrivateKeyLocation)))
                        .build())*/
                .withHttpLogs(LOG::info)
                .withCredentialsCaching()
                .build();
    }

    private OpenTelemetry buildOpenTelemetryConfig(){
        Resource resource = Resource.getDefault().toBuilder()
                .put(SERVICE_NAME, "truelayer-java")
                .put(SERVICE_VERSION, "0.1.0")
                .build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(
                        OtlpGrpcSpanExporter.builder().build()))
                .setResource(resource)
                .build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                // .setMeterProvider(sdkMeterProvider)
                .build();

        return openTelemetry;
    }
}
