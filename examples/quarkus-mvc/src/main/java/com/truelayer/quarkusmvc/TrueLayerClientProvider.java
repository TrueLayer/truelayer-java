package com.truelayer.quarkusmvc;

import com.truelayer.java.*;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

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
                .environment(Environment.sandbox())
                .clientCredentials(ClientCredentials.builder()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .build())
                .signingOptions(SigningOptions.builder()
                        .keyId(signingKeyId)
                        .privateKey(Files.readAllBytes(Path.of(signingPrivateKeyLocation)))
                        .build())
                .withHttpLogs(LOG::info)
                .withCredentialsCaching()
                .build();
    }
}
