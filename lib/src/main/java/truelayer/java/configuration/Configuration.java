package truelayer.java.configuration;

import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@Getter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
public class Configuration {

    private VersionInfo versionInfo;

    private Endpoint authentication;

    private Endpoint hostedPaymentPage;

    private Payments payments;

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    @Accessors(fluent = true)
    public static class VersionInfo {
        private String libraryName;
        private String libraryVersion;
    }

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    @Accessors(fluent = true)
    public static class Payments {
        private List<String> scopes;
        private String endpointUrl;
    }

    @Value
    @EqualsAndHashCode
    @ToString
    @Accessors(fluent = true)
    public static class Endpoint {
        private String endpointUrl;
    }
}
