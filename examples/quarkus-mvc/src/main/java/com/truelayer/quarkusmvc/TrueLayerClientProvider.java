package com.truelayer.quarkusmvc;

import com.truelayer.java.*;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.nio.file.Files;
import java.nio.file.Path;

public class TrueLayerClientProvider {

    private static final Logger LOG = Logger.getLogger("TL SDK Custom logger");

    @ConfigProperty(name = "tl.client.id")
    String clientId;

    @ConfigProperty(name = "tl.client.secret")
    String clientSecret;

    @ConfigProperty(name = "tl.signing.key_id")
    String signingKeyId;

    @ConfigProperty(name = "tl.signing.private_key_location")
    String signingPrivateKeyLocation;

    @Singleton
    @SneakyThrows
    public ITrueLayerClient producer(){

        return TrueLayerClient.New()
                .environment(Environment.sandbox())
                .clientCredentials(
                        ClientCredentials.builder()
                                .clientId(clientId)
                                .clientSecret(clientSecret)
                            .build()
                )
                .signingOptions(SigningOptions.builder()
                        .keyId(signingKeyId)
                        .privateKey(Files.readAllBytes(Path.of(signingPrivateKeyLocation)))
                        .build())
                .withHttpLogs(LOG::info)
                .build();
    }
}
