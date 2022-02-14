package truelayer.java;

import java.net.URI;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Environment {

    private final URI authApiUri;

    private final URI paymentsApiUri;

    private final URI hppUri;

    public static Environment sandbox() {
        return new Environment(
                URI.create("https://auth.truelayer-sandbox.com"),
                URI.create("https://api.truelayer-sandbox.com"),
                URI.create("https://payment.truelayer-sandbox.com"));
    }

    public static Environment live() {
        return new Environment(
                URI.create("https://auth.truelayer.com"),
                URI.create("https://api.truelayer.com"),
                URI.create("https://payment.truelayer.com"));
    }

    public static Environment custom(URI authApiUri, URI paymentsApiUri, URI hppUri) {
        return new Environment(authApiUri, paymentsApiUri, hppUri);
    }
}
