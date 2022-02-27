package com.truelayer.java;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;

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
    private String keyId;

    private byte[] privateKey;

    public static class SigningOptionsBuilder {

        public SigningOptions build() {
            if (ObjectUtils.isEmpty(this.keyId)) {
                throw new TrueLayerException("key id must be set");
            }
            if (ObjectUtils.isEmpty(this.privateKey)) {
                throw new TrueLayerException("private key must be set");
            }
            return new SigningOptions(keyId, privateKey);
        }
    }
}
