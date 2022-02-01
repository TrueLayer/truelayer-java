package truelayer.java;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class SigningOptions {
    private String keyId;

    private byte[] privateKey;
}
