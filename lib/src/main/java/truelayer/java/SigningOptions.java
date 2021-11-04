package truelayer.java;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SigningOptions {
    private String keyId;

    //todo: use a proper type
    private byte[] privateKey;
}
