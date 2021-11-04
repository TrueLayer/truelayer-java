package truelayer.java;

import lombok.Builder;

@Builder
public class SigningOptions {
    private String keyId;

    //todo: use a proper type
    private String privateKey;
}
