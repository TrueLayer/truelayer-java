package truelayer.java;

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
    private String keyId;

    private byte[] privateKey;

    // todo add custom build with validation
}
