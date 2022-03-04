package com.truelayer.java;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Class that models TrueLayer signing options required for payments.
 * It should be built with the help of its builder class.
 *
 * @see SigningOptionsBuilder
 */
@Builder
@Getter
@Accessors(fluent = true)
public class SigningOptions {

    @NotEmpty(message = "key id must be set")
    private String keyId;

    @NotEmpty(message = "private key must be set")
    private byte[] privateKey;

    public static class SigningOptionsBuilder {

        public SigningOptions build() {
            SigningOptions signingOptions = new SigningOptions(keyId, privateKey);
            Utils.validateObject(signingOptions);
            return signingOptions;
        }
    }
}
