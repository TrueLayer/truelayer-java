package truelayer.java;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class SigningOptions {
    private String keyId;

    // todo: use a proper type
    private byte[] privateKey;
}
