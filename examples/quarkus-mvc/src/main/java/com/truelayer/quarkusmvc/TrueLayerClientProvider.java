package com.truelayer.quarkusmvc;

import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import truelayer.java.ClientCredentials;
import truelayer.java.ITrueLayerClient;
import truelayer.java.SigningOptions;
import truelayer.java.TrueLayerClient;

import javax.inject.Singleton;
import java.nio.file.Files;
import java.nio.file.Path;

public class TrueLayerClientProvider {

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
                .withEnvironment(Environment.sandbox())
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
                .build();
    }
}
