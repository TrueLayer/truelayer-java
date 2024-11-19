package com.truelayer.java;

import java.net.URI;
import java.text.MessageFormat;
import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Environment {

    private final URI authApiUri;

    private final URI paymentsApiUri;

    private final URI hppUri;

    private static final String PAYMENTS_API_DEFAULT_VERSION = "v3";

    /**
     * Builder for development environment
     * @return a development environment object
     */
    public static Environment development() {
        return new Environment(
                URI.create("https://auth.t7r.dev"),
                URI.create(MessageFormat.format("https://api.t7r.dev/{0}/", PAYMENTS_API_DEFAULT_VERSION)),
                URI.create("https://payment.t7r.dev"));
    }

    /**
     * Builder for sandbox environment
     * @return a sandbox environment object
     */
    public static Environment sandbox() {
        return new Environment(
                URI.create("https://auth.truelayer-sandbox.com"),
                URI.create(
                        MessageFormat.format("https://api.truelayer-sandbox.com/{0}/", PAYMENTS_API_DEFAULT_VERSION)),
                URI.create("https://payment.truelayer-sandbox.com"));
    }

    /**
     * Builder for production environment
     * @return a production environment object
     */
    public static Environment live() {
        return new Environment(
                URI.create("https://auth.truelayer.com"),
                URI.create(MessageFormat.format("https://api.truelayer.com/{0}/", PAYMENTS_API_DEFAULT_VERSION)),
                URI.create("https://payment.truelayer.com"));
    }

    /**
     * Custom environment builder. Meant for testing purposes
     * @param authApiUri the authentication API endpoint
     * @param paymentsApiUri the Payments API endpoint
     * @param hppUri the <i>Hosted Payment Page</i> endpoint
     * @return a custom environment object
     */
    public static Environment custom(URI authApiUri, URI paymentsApiUri, URI hppUri) {
        return new Environment(authApiUri, paymentsApiUri, hppUri);
    }
}
